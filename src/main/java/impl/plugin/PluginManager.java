package impl.plugin;


import impl.plugin.pluginlibraries.*;
import impl.utils.Utils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class PluginManager {
    private final Logger logger = LoggerFactory.getLogger(PluginManager.class);
    public boolean running = false;
    private boolean noPlugins = false;
    private final List<Hook> hookList = new ArrayList<>();
    private final List<Plugin> pluginList;
    private final Context engine;
    private final Value bindings;

    public PluginManager() {
        this.pluginList = Utils.getPlugins();

        if(pluginList == null) {
            this.noPlugins = true;
            this.engine = null;
            this.bindings = null;
            return;
        }

        this.engine = Context.newBuilder("js")
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLookup(className -> true)
                .build();

        this.bindings = engine.getBindings("js");
        this.bindings.putMember("core", new PluginCore(this));
        this.bindings.putMember("config", new PluginConfig());
        this.bindings.putMember("http", new PluginHTTP(this));
        this.bindings.putMember("websocket", new PluginWS());
        this.bindings.putMember("database", new PluginDatabase());
        this.bindings.putMember("gzip", new PluginGZIP());



        for(Plugin plugin : pluginList) {
            logger.info(plugin.getPluginName());
            engine.eval("js", plugin.getPluginScript());
        }
    }


    public void hookLoop() {
        if(noPlugins){return;}
        this.running = true;

        for (var hook : hookList) {
            if(hook.getHookName().equalsIgnoreCase("@init")) {
                try {
                    hook.getFunction().call();
                } catch (Exception e) {logger.error(e.getMessage());}
            }
        }

        while (this.running) {
            for (var hook : hookList) {
                if(hook.getHookName().equalsIgnoreCase("@update")) {
                    try {
                        hook.getFunction().call();
                    } catch (Exception e) {logger.error(e.getMessage());}
                }
            }
        }


    }

    public void createHook(String hookName, Callable function) {
        hookList.add(new Hook(hookName, function));
    }

    public void triggerHook(String hookName) {
        for (var hook : hookList) {
            if(hook.getHookName().equalsIgnoreCase(hookName)) {
                try {
                    hook.getFunction().call();
                } catch (Exception e) {logger.error(e.getMessage());}
            }
        }
    }

    public void addToEngine(String name, Object val) {
        this.bindings.putMember(name, val);
    }

    public void removeFromEngine(String name) {
        this.bindings.putMember(name, null);
    }
}
