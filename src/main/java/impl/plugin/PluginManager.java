package impl.plugin;


import impl.json.ConfigJson;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.FileHandler;

public class PluginManager {
    private final Logger logger = LoggerFactory.getLogger(PluginManager.class);
    public boolean running = false;
    private boolean noPlugins = false;
    private final List<Hook> hookList = new ArrayList<>();
    private final List<Plugin> pluginList;
    private final Context engine;
    private final Value bindings;
    private final ConfigJson config;
    private FileHandler logFile = null;

    public PluginManager(ConfigJson config) {
        this.pluginList = Utils.getPlugins();
        this.config = config;

        if(pluginList == null) {
            this.noPlugins = true;
            this.engine = null;
            this.bindings = null;
            return;
        }

        try {
            this.logFile = new FileHandler(config.getGraalvmLogFile());
        } catch (Exception e) {logger.error(e.getMessage());}



        this.engine = Context.newBuilder("js")
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLookup(className -> true)
                .logHandler(this.logFile)
                .build();

        this.bindings = engine.getBindings("js");
        this.bindings.putMember("NotesBin", Global.notesBin);
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
