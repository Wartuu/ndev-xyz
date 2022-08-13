package impl.utils;

import impl.json.ConfigJson;
import impl.utils.finals.Global;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class HtmlParser {

    private static final String configDomain = "@config.domain";
    private static final String configHttpPort = "@config.http.port";
    private static final String configWebsocketPort = "@config.websocket.port";
    private static final String configMainVersion = "@config.version.main";
    private static final String configFrontendVersion = "@config.version.frontend";
    private static final String configBackendVersion = "@config.version.backend";
    private static final String httpTotalRuntime = "@main.runtime";

    public static String parse(String html) {
        ConfigJson config = Utils.getConfig(Global.configName);

        html = html.replaceAll(configDomain, config.getServerDomain());
        html = html.replaceAll(configHttpPort, String.valueOf(config.getHttpPort()));
        html = html.replaceAll(configWebsocketPort, String.valueOf(config.getWebsocketPort()));
        html = html.replaceAll(configMainVersion, config.getMainVersion());
        html = html.replaceAll(configBackendVersion, config.getBackendVersion());
        html = html.replaceAll(configFrontendVersion, config.getFrontendVersion());


        long time = Duration.between(Global.startTime, Instant.now()).toMillis();

        long HH = TimeUnit.MILLISECONDS.toHours(time);
        long MM = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
        long SS = TimeUnit.MILLISECONDS.toSeconds(time) % 60;

        html = html.replaceAll(httpTotalRuntime, String.format("%02d:%02d:%02d", HH, MM, SS));

        return html;
    }
}
