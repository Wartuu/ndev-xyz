package impl.plugin.pluginlibraries;

import impl.json.ConfigJson;
import impl.plugin.PluginManager;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class PluginCore {
    private PluginManager pluginManager;
    private ConfigJson configJson;
    public Logger logger = LoggerFactory.getLogger(PluginCore.class);

    public PluginCore(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
        this.configJson = Utils.getConfig(Global.configName);
    }

    public void createHook(String hookName, Callable function) {
        this.pluginManager.createHook(hookName, function);
    }

}
