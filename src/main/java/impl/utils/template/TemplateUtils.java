package impl.utils.template;

import impl.json.ConfigJson;
import impl.utils.Hardware;
import impl.utils.finals.Global;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class TemplateUtils {
    public TemplateEngine engine;
    private ConfigJson config;

    public TemplateUtils(ConfigJson config) {
        this.config = config;

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("/pages/");
        resolver.setSuffix(".html");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(resolver);

        this.engine = templateEngine;
    }

    public Context getDefaultContext() {
        Context context = new Context();
        context.setVariable("config", config);


        long time = Duration.between(Global.startTime, Instant.now()).toMillis();

        long HH = TimeUnit.MILLISECONDS.toHours(time);
        long MM = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
        long SS = TimeUnit.MILLISECONDS.toSeconds(time) % 60;

        context.setVariable("runtime", String.format("%02d:%02d:%02d", HH, MM, SS));


        return context;
    }

    public String processDefault(String file) {

        return this.engine.process(file, getDefaultContext());
    }
}
