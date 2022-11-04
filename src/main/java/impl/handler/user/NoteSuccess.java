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
        String qrThing = Utils.generateQrCodeToBase64(Utils.getQuery(exchange, "text"));

        Context context = Global.templateUtils.getDefaultContext();
        context.setVariable("QR", qrThing);

        Utils.sendOutput(exchange, Global.templateUtils.engine.process(template, context), false, 200);

    }
}
