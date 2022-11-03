package impl.handler.api.v1;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.Utils;
import impl.utils.finals.Global;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class ServerRuntime implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        long time = Duration.between(Global.startTime, Instant.now()).toMillis();

        long HH = TimeUnit.MILLISECONDS.toHours(time);
        long MM = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
        long SS = TimeUnit.MILLISECONDS.toSeconds(time) % 60;

        Utils.sendOutput(exchange, String.format("%02d:%02d:%02d", HH, MM, SS), false, 200);
    }
}
