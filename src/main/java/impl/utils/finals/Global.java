package impl.utils.finals;

import impl.utils.Database;
import impl.utils.Utils;

import java.time.Instant;

public class Global {
    public static final String configName = "config.json";
    public static final Instant startTime = Instant.now();
    public static Database database;
}
