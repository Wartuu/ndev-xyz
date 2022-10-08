package impl.utils.executor;

import impl.json.ConfigJson;
import impl.utils.finals.Global;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

public class ExecutorThreadPool extends ThreadPoolExecutor {
    private ConfigJson config;

    public ExecutorThreadPool(ConfigJson configJson, int coreThreads, int maxThreads) {
        super(coreThreads, maxThreads, configJson.getExecutorMaxIdleTime(), TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        this.config = configJson;
    }

    @Override
    public void execute(@NotNull Runnable command) {
        Global.requestStatistics.mark();
        super.execute(command);
    }
}
