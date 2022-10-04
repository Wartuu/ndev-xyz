package impl.plugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import impl.json.ConfigJson;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginIRC {
    private PluginManager pluginManager;
    private ConfigJson config;
    private String rawConfig;
    private JsonObject configObject;
    public Logger logger = LoggerFactory.getLogger(PluginIRC.class);

    public PluginIRC(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
        this.config = Utils.getConfig(Global.configName);
        this.rawConfig = Utils.getConfigRaw(Global.configName);
        this.configObject = new JsonParser().parse(rawConfig).getAsJsonObject();
    }
}
