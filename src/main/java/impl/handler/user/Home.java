package impl.handler.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.html.HtmlParser;
import impl.utils.Utils;
import impl.utils.html.ScriptValue;
import impl.utils.html.ValueType;
import org.objectweb.asm.tree.analysis.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Home implements HttpHandler {

    private final String content;
    public Home(String path) {
        String file = Utils.getFile(path);
        List<ScriptValue> scriptValues = new ArrayList<>();
        scriptValues.add(new ScriptValue("test", "works?", ValueType.TEXT));
        scriptValues.add(new ScriptValue("test2", "2", ValueType.NUMBER));

        file = HtmlParser.addScriptValues(scriptValues, file);
        this.content = HtmlParser.parse(file);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.sendOutput(exchange, content, false, 200);
    }
}
