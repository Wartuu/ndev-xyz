package impl.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;


public class PluginManager {
    public final Logger logger = LoggerFactory.getLogger(PluginManager.class);
    public boolean running = false;


    public PluginManager() {
        for(var engine : new ScriptEngineManager().getEngineFactories()) {
            logger.info(engine.getEngineName());
            logger.info(engine.getEngineVersion());
            logger.info(engine.getLanguageName());
            logger.info("---------------------------");
        }


        this.running = true;
    }
    public void initPlugins(){}
    public void hookLoop(){}
    public void triggerHook(String name) {}


}
