package impl.plugin;

public class Plugin {
    private String name;
    private String content;

    public Plugin(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
