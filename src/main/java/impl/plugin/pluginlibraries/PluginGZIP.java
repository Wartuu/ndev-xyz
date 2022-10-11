package impl.plugin.pluginlibraries;

import com.sun.net.httpserver.HttpExchange;
import impl.utils.Utils;
import impl.utils.gzip.Gzip;

public class PluginGZIP {
    public byte[] createGzip(String value) {
        return Gzip.createGzip(value);
    }

    public boolean isSupportedGzip(HttpExchange exchange) {
        return Utils.doesSupportGzip(exchange);
    }
}
