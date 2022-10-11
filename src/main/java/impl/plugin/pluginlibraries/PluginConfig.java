package impl.plugin.pluginlibraries;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import impl.utils.Utils;
import impl.utils.finals.Global;

public class PluginConfig {

    private JsonObject configObject;
    public PluginConfig() {
        this.configObject = new JsonParser().parse(Utils.getConfigRaw(Global.configName)).getAsJsonObject();
    }


    public String get(String key) {
        return this.configObject.get(key).getAsString();
    }
}
