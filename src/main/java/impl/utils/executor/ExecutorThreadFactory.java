package impl.utils.executor;

import com.sun.net.httpserver.HttpHandler;
import impl.json.ConfigJson;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;

public class  ExecutorThreadFactory implements ThreadFactory {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorThreadFactory.class);
    private static ConfigJson config;


    public ExecutorThreadFactory(ConfigJson cfg) {
        config = cfg;
    }


    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            private final Logger threadLogger = LoggerFactory.getLogger(thread.getClass());
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                threadLogger.error(e.getMessage());
            }
        });

        thread.setPriority(config.getExecutorWorkerPriority());
        return thread;
    }
}
