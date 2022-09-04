package impl.plugin;

import jdk.nashorn.internal.runtime.ScriptFunction;

import java.util.concurrent.Callable;

public class FunctionHook {
    private String hookName;
    private Callable function;

    public FunctionHook(String hookName, Callable function) {
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
