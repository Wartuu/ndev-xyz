package impl.plugin;

import java.util.concurrent.Callable;

public class Hook {
    private final String hookName;
    private final Callable function;

    public Hook(String hookName, Callable function) {
        this.hookName = hookName;
        this.function = function;
    }

    public String getHookName() {
        return hookName;
    }

    public Callable getFunction() {
        return function;
    }
}
