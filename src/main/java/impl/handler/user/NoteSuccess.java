package impl.handler.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.json.ConfigJson;
import impl.utils.Utils;
import impl.utils.finals.Global;
import kotlin.Pair;
import org.thymeleaf.context.Context;

import java.io.IOException;

public class NoteSuccess implements HttpHandler {

    private final String template;
    private final ConfigJson config;
    private final String qrPrefix;

    public NoteSuccess(String template, ConfigJson configJson) {
        this.template = template;
        this.config = configJson;

        if(config.isAddPortToRedirection()) {
            this.qrPrefix = "http://" + config.getServerDomain() + ":" + configJson.getHttpPort() + "/note/";
        } else {
            this.qrPrefix = "http://" + config.getServerDomain() + "/note/";
        }

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String linkQuery = Utils.getQuery(exchange, "note");

        if(linkQuery == null) {
            Utils.sendOutput(exchange, "error: no given link", false, 200);
            return;
        }

        Context context = Global.templateUtils.getDefaultContext();

        if(config.isAddPortToRedirection()) {
            context.setVariable("QR", Utils.generateQrCodeToBase64(qrPrefix + linkQuery));
        } else {
            context.setVariable("QR", Utils.generateQrCodeToBase64(qrPrefix + linkQuery));
        }

        context.setVariable("link", qrPrefix + linkQuery);
        context.setVariable("burnAfterRead", true);

        Utils.sendOutput(exchange, Global.templateUtils.engine.process(template, context), false, 200);

    }
}
