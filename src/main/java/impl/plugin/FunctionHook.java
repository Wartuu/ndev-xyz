package impl.plugin;


import java.util.concurrent.Callable;

public class FunctionHook {
    private String hookName;
    private Callable function;

    public FunctionHook(String hookName, Callable<Object> function) {
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
