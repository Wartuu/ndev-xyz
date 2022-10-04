package impl.utils.html;

public class ScriptValue {
    public String key, val;
    public ValueType type;

    public ScriptValue(String key, String val, ValueType type) {
        this.key = key;
        this.val = val;
        this.type = type;
    }
}
