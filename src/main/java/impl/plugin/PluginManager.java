package impl.plugin;

import impl.HttpService;
import impl.utils.Utils;
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

    public PluginManager(HttpService httpService) {
        engine = new ScriptEngineManager().getEngineByName("JavaScript");
        invEngine = (Invocable) engine;

        if(Utils.getPlugins() == null) {
            logger.info("no plugins detected");
        }

        engine.put("Nekodev", new PluginFunctions(this, httpService));

        for(String pluginFile : Utils.getPlugins()) {
            String pluginScript = Utils.getResource("plugins/" + pluginFile);

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
        for (FunctionHook hook : functionHooks) {
            try {
                if(hook.getHookName().equals("@init")) {
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
}