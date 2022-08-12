package impl.utils;

import impl.json.Config;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    private static Connection connection;



    public static void connect() {
        try {
            Config cfg = Utils.getConfig(Global.configName);
            connection = DriverManager.getConnection(cfg.getDatabaseUrl(), cfg.getDatabaseUsername(), cfg.getDatabasePassword());

            if (connection != null) {
                logger.info("Connected to database");
            } else {
                logger.error("Connection failed");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String execute(String command) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(command);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
