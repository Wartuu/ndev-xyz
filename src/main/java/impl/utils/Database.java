package impl.utils;
import com.sun.net.httpserver.HttpExchange;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import impl.database.Account;
import impl.handler.api.v1.Register;
import impl.json.ConfigJson;
import impl.json.LoginJson;
import impl.json.RegisterJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.nimbus.State;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Properties;
import java.util.Random;


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



    public boolean containsIllegalCharacter(String s) {
        for (String illegalCharacter : illegalUsernameSimbols) {
            if(s.contains(illegalCharacter)) {
                logger.warn("illegal character in username: " + s + " => " + illegalCharacter);
                return true;

            }
        }

        return false;
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
                account.setSession(resultSet.getString("session_token"));
                account.setAuthToken(resultSet.getString("auth_token"));

                return account;
            }



        } catch (SQLException e) {logger.error(e.getMessage());}

        return null;
    }

    public Account getAccountByUsername(String username) {
        try {

            if(containsIllegalCharacter(username)) {return null;}

            Statement statement = connection.createStatement();
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
                account.setSession(resultSet.getString("session_token"));
                account.setAuthToken(resultSet.getString("auth_token"));
                return account;
            }



        } catch (SQLException e) {logger.error(e.getMessage());}

        return null;
    }

    public Account getAccountBySession(String session) {
        try {

            if(containsIllegalCharacter(session)) {return null;}

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from account where session_token='" + session + "'");


            while (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getLong("id"));
                account.setUsername(resultSet.getString("username"));
                account.setPassword(resultSet.getString("password"));
                account.setLastLogin(resultSet.getDate("last_login"));
                account.setLastIp(resultSet.getString("last_ip"));
                account.setAccountCreated(resultSet.getDate("account_created"));
                account.setAccountType(resultSet.getInt("account_type"));
                account.setChatAccess(resultSet.getBoolean("chat_access"));
                account.setSession(session);
                account.setAuthToken(resultSet.getString("auth_token"));
                return account;
            }



        } catch (SQLException e) {logger.error(e.getMessage());}

        return null;
    }

    public Account getAccountByAuthToken(String authToken) {
        try {

            if(containsIllegalCharacter(authToken)) {return null;}

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from account where auth_token='" + authToken + "'");


            while (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getLong("id"));
                account.setUsername(resultSet.getString("username"));
                account.setPassword(resultSet.getString("password"));
                account.setLastLogin(resultSet.getDate("last_login"));
                account.setLastIp(resultSet.getString("last_ip"));
                account.setAccountCreated(resultSet.getDate("account_created"));
                account.setAccountType(resultSet.getInt("account_type"));
                account.setChatAccess(resultSet.getBoolean("chat_access"));
                account.setSession(resultSet.getString("session_token"));
                account.setAuthToken(authToken);
                return account;
            }



        } catch (SQLException e) {logger.error(e.getMessage());}

        return null;
    }


    public RegisterJson createNewAccount(HttpExchange exchange, String username, String password, int accountType) {
        RegisterJson registerJson = new RegisterJson();

        try {

            if(username == null || password == null) {
                registerJson.setReason("provided null value");
                registerJson.setSuccess(false);
                return registerJson;
            }

            if(username.length() < 5 || username.length() > 15) {
                registerJson.setReason("too long/short username");
                registerJson.setSuccess(false);
                return registerJson;
            }

            if(password.length() < 5 || password.length() > 128) {
                registerJson.setReason("too long/short password");
                registerJson.setSuccess(false);
                return registerJson;
            }
            if(containsIllegalCharacter(username)) {registerJson.setSuccess(false); registerJson.setReason("illegal character in username"); return registerJson;}
            if(containsIllegalCharacter(password)) {registerJson.setSuccess(false); registerJson.setReason("illegal character in password"); return registerJson;}


            Statement statement = connection.createStatement();

            Random random = new Random();
            String auth = random.ints(97,123).limit(32).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            String session = random.ints(97,123).limit(128).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();


            ResultSet usernameExistsRs = statement.executeQuery("select * from account where UPPER(username) = UPPER('" + username + "')");

            if(usernameExistsRs.next()) {
                registerJson.setSuccess(false);
                registerJson.setReason("username is already taken");
                return registerJson;
            }
            while (statement.executeQuery("select * from account where auth_token = '" + auth + "'").next()) {
                auth = random.ints(97,123).limit(32).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            }

            while (statement.executeQuery("select * from account where session_token = '" + session + "'").next()) {
                session = random.ints(97,123).limit(128).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            }


            statement.executeUpdate(
                    "insert into account " +
                            "(username, password, account_created, last_ip, last_login, account_type, chat_access, auth_token, session_token)" +
                            " values ('"+username + "','" + password + "', now(), '"+exchange.getRemoteAddress().getAddress().getHostAddress()+"', now(), " + accountType + ", true, '" + auth + "','" + session + "')"
            );

            registerJson.setReason("");
            registerJson.setSuccess(true);
            registerJson.setAuthToken(auth);
            registerJson.setSessionToken(session);
            return registerJson;

        } catch (SQLException e) {logger.error(e.getMessage()); registerJson.setSuccess(false); registerJson.setReason("database error try again later"); return registerJson;}
    }


    public Account generateNewSession(Account account) {
        try {
            Random random = new Random();
            String session = random.ints(97,123).limit(128).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

            Statement statement = connection.createStatement();

            statement.executeUpdate(
                    "update account " +
                            "set session_token = '" + session + "' " +
                            "where id=" + account.getId()

            );

            account.setSession(session);
            return account;

        } catch (SQLException e) {logger.error(e.getMessage());};
        return null;
    }

    public Account deleteSession(Account account) {
        try {
            Random random = new Random();
            String session = random.ints(97,123).limit(128).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

            Statement statement = connection.createStatement();

            statement.executeUpdate(
                    "update account " +
                            "set session_token = NULL " +
                            "where id=" + account.getId()

            );

            account.setSession(null);
            return account;

        } catch (SQLException e) {logger.error(e.getMessage());};
        return null;
    }
}