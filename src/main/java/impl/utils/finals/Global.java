package impl.utils.finals;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import impl.database.Database;
import impl.plugin.PluginManager;
import impl.utils.IPCollector;
import impl.utils.notesbin.NotesBin;

import java.time.Instant;

public class Global {
    public static final String configName = "config.json";
    public static final Instant startTime = Instant.now();
    public static final IPCollector ipCollector = new IPCollector();
    public static final Gson gson = new Gson();
    public static final JsonParser jsonParser = new JsonParser();


    public static Database database;
    public static PluginManager pluginManager;
    public static NotesBin notesBin;
}
