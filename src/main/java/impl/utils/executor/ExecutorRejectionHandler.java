package impl.utils.executor;

import impl.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class ExecutorRejectionHandler implements RejectedExecutionHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpService.class);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        logger.warn("executor rejected execution of " + r.getClass().toString());
    }
}
