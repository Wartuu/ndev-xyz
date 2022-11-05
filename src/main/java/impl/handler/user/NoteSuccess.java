package impl.handler.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.thymeleaf.context.Context;

import java.io.IOException;

public class NoteSuccess implements HttpHandler {

    private final String template;

    public NoteSuccess(String template) {
        this.template = template;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String linkQuery = Utils.getQuery(exchange, "link");

        if(linkQuery == null) {
            Utils.sendOutput(exchange, "error: no given link", false, 200);
            return;
        }

        Context context = Global.templateUtils.getDefaultContext();
        context.setVariable("QR", Utils.generateQrCodeToBase64(linkQuery));
        context.setVariable("link", linkQuery);

        Utils.sendOutput(exchange, Global.templateUtils.engine.process(template, context), false, 200);

    }
}
