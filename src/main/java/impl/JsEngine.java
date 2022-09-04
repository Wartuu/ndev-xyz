package impl;

import com.sun.net.httpserver.HttpServer;
import impl.json.ConfigJson;
import impl.utils.Utils;
import impl.plugin.PluginFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JsEngine {

    private ConfigJson config;
    private HttpServer httpServer;
    private Logger logger = LoggerFactory.getLogger(JsEngine.class);

    private ScriptEngine engine;

    public JsEngine(ConfigJson cfg, HttpServer httpServer) {
        this.config = cfg;
        this.httpServer = httpServer;
        this.engine = new ScriptEngineManager().getEngineByName("JavaScript");
    }

    public void execute(String script) {
        try {
            engine.eval(script);
        } catch (Exception e) {logger.error(e.getMessage());};

    }
}
