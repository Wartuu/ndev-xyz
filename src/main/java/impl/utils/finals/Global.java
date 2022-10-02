package impl.utils.finals;

import com.google.gson.Gson;
import impl.database.Database;
import impl.plugin.PluginManager;
import impl.utils.IPCollector;
import impl.utils.executor.notesbin.NotesBin;

import java.time.Instant;

public class Global {
    public static final String configName = "config.json";
    public static final Instant startTime = Instant.now();
    public static Database database;
    public static PluginManager pluginManager;
    public static NotesBin notesBin;
    public static IPCollector ipCollector = new IPCollector();
    public static Gson gson = new Gson();
}
