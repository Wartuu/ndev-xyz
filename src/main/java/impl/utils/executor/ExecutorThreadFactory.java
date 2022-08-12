package impl.utils.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;

public class ExecutorThreadFactory implements ThreadFactory {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorThreadFactory.class);


    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        logger.info(" created new executor thread => [" + thread.getName() + ":id" + thread.getId() + "]");

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            private final Logger threadLogger = LoggerFactory.getLogger(thread.getClass());
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                threadLogger.error(e.getMessage());
            }
        });

        thread.setPriority(Thread.MAX_PRIORITY);

        return thread;
    }
}
