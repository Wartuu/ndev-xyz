package impl.plugin;

import impl.HttpService;
import impl.WebsocketService;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;



public class PluginManager {
    public final Logger logger = LoggerFactory.getLogger(PluginManager.class);
    private final List<FunctionHook> functionHooks = new ArrayList<>();
    private final List<Plugin> pluginList = new ArrayList<>();
    private ScriptEngine engine;
    private Invocable invEngine;
    public boolean running = false;


    public PluginManager() {
        engine = new ScriptEngineManager().getEngineByName("JavaScript");
        invEngine = (Invocable) engine;

        if(Utils.getPlugins() == null) {
            logger.info("no plugins detected");
        }

        engine.put("plugin", new PluginFunctions(this));
        engine.put("database", Global.database);
        engine.put("irc", new PluginIRC(this));


        for(String pluginFile : Utils.getPlugins()) {
            String pluginScript = Utils.getFile("plugins/" + pluginFile);

            try {
                engine.eval(pluginScript);
            } catch (Exception e) {
                logger.error("---------->" + pluginFile + "<----------");
                logger.error(e.getMessage());
                logger.error("----------> END OF " + pluginFile + " ERROR LOG <----------");
            }

            pluginList.add(new Plugin(pluginFile, pluginScript));
        }
    }

    private void loadPlugins() {
        logger.info("loading plugins...");
        for (FunctionHook hook : functionHooks) {
            try {
                if(hook.getHookName().equals("@load")) {
                    hook.getFunction().call();
                }
            } catch (Exception e) {
                logger.error("---------->" + hook.getHookName() + "<----------");
                logger.error(e.getMessage());
                logger.error(hook.getFunction().toString());
                logger.error("----------> END OF " + hook.getHookName() + " ERROR LOG <----------");
            }

        }
    }

    public void hookLoop() {
        loadPlugins();
        this.running = true;

        while (true) {

            for (FunctionHook hook : functionHooks) {
                try {
                    if(hook.getHookName().equals("@update")) {
                        hook.getFunction().call();
                    }
                } catch (Exception e) {
                    logger.error("---------->" + hook.getHookName() + "<----------");
                    logger.error(e.getMessage());
                    logger.error("----------> END OF " + hook.getHookName() + " ERROR LOG <----------");
                }

            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void createHook(String name, Callable function) {
        functionHooks.add(new FunctionHook(name, function));
    }

    public void triggerHook(String name) {
        for (FunctionHook hook : functionHooks) {
            if(hook.getHookName().equals(name)) {
                try {
                    hook.getFunction().call();
                } catch (Exception e) {
                    logger.error("---------->" + hook.getHookName() + "<----------");
                    logger.error(e.getMessage());
                    logger.error("----------> END OF " + hook.getHookName() + " ERROR LOG <----------");
                }
            }
        }
    }

    public void triggerHook(String name, Object variable) {
        for (FunctionHook hook : functionHooks) {
            if(hook.getHookName().equals(name)) {
                try {
                    hook.getFunction().call();
                } catch (Exception e) {
                    logger.error("---------->" + hook.getHookName() + "<----------");
                    logger.error(e.getMessage());
                    logger.error("----------> END OF " + hook.getHookName() + " ERROR LOG <----------");
                }
            }
        }
    }
}
