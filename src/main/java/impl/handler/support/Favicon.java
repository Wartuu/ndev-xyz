package impl.handler.support;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.Utils;
import impl.utils.gzip.Gzip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Favicon implements HttpHandler {
    private Logger logger = LoggerFactory.getLogger(Favicon.class);
    private byte[] faviconContent;
    private byte[] faviconGzipContent;
    private boolean gzip;
    private String mimeType;

    public Favicon(byte[] faviconFile) {
        this.faviconContent = faviconFile;
        this.mimeType = Utils.getMimeMap().get("ico");
        byte[] gzipContent = Gzip.createGzip(faviconFile);

        logger.info("favicon compression, gzip: " + gzipContent.length + "b, default: " + faviconContent.length + "b");
        if(gzipContent.length < faviconFile.length) {
            logger.info("using gzip");
            gzip = true;
            faviconGzipContent = gzipContent;
        } else {
            logger.info("using default");
            gzip = false;
        }



    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(gzip && Utils.doesSupportGzip(exchange)) {
            exchange.getResponseHeaders().set("Content-Encoding", "gzip");
            exchange.getResponseHeaders().set("Transfer-Encoding", "gzip");
            exchange.getResponseHeaders().set("Content-Type", mimeType);
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");

            Utils.sendBytesOutput(exchange, faviconGzipContent, 200);
        } else {
            exchange.getResponseHeaders().set("Content-Type", mimeType);
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");

            Utils.sendBytesOutput(exchange, faviconContent, 200);
        }
    }
}
