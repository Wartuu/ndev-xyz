package impl.utils;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import impl.database.Account;
import impl.json.ConfigJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;


public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    private static ConfigJson config;

    private static Properties connectionProperties = new Properties();
    private Connection connection;
    private static SchemaParser schemaParser = new SchemaParser();
    private static SchemaGenerator schemaGenerator = new SchemaGenerator();

    private static final String[] illegalUsernameSimbols = {"=", "'", "\"", "\\", "/", ",", "\0", "\b", "\n", "\r", "\t", "%", "$", "*"};

    public Database(ConfigJson cfg) {
        config = cfg;
    }


    public void connect() {

        connectionProperties.setProperty("password", config.getDatabasePassword());
        connectionProperties.setProperty("user", config.getDatabaseUsername());

        logger.info("connecting to postgresql database at url: " + config.getDatabaseUrl());

        try {
            connection = DriverManager.getConnection(config.getDatabaseUrl(), connectionProperties);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return;
        }

        logger.info("succeed connecting to database");
    }

    public String execute(String command, String entryValue) {
        StringBuilder output = new StringBuilder();
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(command);


            while (resultSet.next()) {
                output.append(resultSet.getString(entryValue));
            }

            logger.info(output.toString());
            return output.toString();


        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return output.toString();
    }

    public Account getAccountById(long id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from account where id=" + id);

            while (resultSet.next()) {
                Account account = new Account();
                account.setId(id);
                account.setUsername(resultSet.getString("username"));
                account.setPassword(resultSet.getString("password"));
                account.setLastLogin(resultSet.getDate("last_login"));
                account.setLastIp(resultSet.getString("last_ip"));
                account.setAccountCreated(resultSet.getDate("account_created"));
                account.setAccountType(resultSet.getInt("account_type"));
                account.setChatAccess(resultSet.getBoolean("chat_access"));
                return account;
            }



        } catch (SQLException e) {logger.error(e.getMessage());}

        return null;
    }

    public Account getAccountByUsername(String username) {
        try {

            for (String illegalCharacter : illegalUsernameSimbols) {
                if(username.contains(illegalCharacter)) {
                    logger.warn("illegal character in username: " + username + " => " + illegalCharacter);
                    return null;

                }
            }

            Statement statement = connection.createStatement();

            logger.debug("select * from account where username='" + username + "'");

            ResultSet resultSet = statement.executeQuery("select * from account where username='" + username + "'");


            while (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getLong("id"));
                account.setUsername(username);
                account.setPassword(resultSet.getString("password"));
                account.setLastLogin(resultSet.getDate("last_login"));
                account.setLastIp(resultSet.getString("last_ip"));
                account.setAccountCreated(resultSet.getDate("account_created"));
                account.setAccountType(resultSet.getInt("account_type"));
                account.setChatAccess(resultSet.getBoolean("chat_access"));
                return account;
            }



        } catch (SQLException e) {logger.error(e.getMessage());}

        return null;
    }

}