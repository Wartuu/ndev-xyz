package impl.handler.support;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.Utils;

import java.io.IOException;

public class Favicon implements HttpHandler {
    private byte[] faviconFile;
    public Favicon(byte[] faviconFile) {
        this.faviconFile = faviconFile;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.sendBytesOutput(exchange, faviconFile, 200);
    }
}
