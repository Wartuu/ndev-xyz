package impl.plugin;

public class Plugin {
    private final String pluginName, pluginScript;

    public Plugin(String pluginName, String pluginScript) {
        this.pluginName = pluginName;
        this.pluginScript = pluginScript;
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getPluginScript() {
        return pluginScript;
    }
}
