package impl.plugin.pluginlibraries;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import impl.json.ConfigJson;
import impl.plugin.PluginManager;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class PluginCore {
    private PluginManager pluginManager;
    public Logger logger = LoggerFactory.getLogger(PluginCore.class);

    public PluginCore(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public void createHook(String hookName, Callable function) {
        this.pluginManager.createHook(hookName, function);
    }
    public void triggerHook(String hookName) {this.pluginManager.triggerHook(hookName);}

    public String jsonToString(Object obj) {return Global.gson.toJson(obj, obj.getClass());}
}
