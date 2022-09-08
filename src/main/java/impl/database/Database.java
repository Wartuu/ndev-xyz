package impl.database;
import com.sun.net.httpserver.HttpExchange;
import impl.json.ConfigJson;
import impl.json.RegisterJson;
import impl.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    private static ConfigJson config;

    private static Properties connectionProperties = new Properties();
    private Connection connection;

    private Pattern usernamePattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

    public Database(ConfigJson cfg) {
        config = cfg;
    }


    public boolean findSpecialCharacter(String value) {
        Matcher matcher = usernamePattern.matcher(value);
        return matcher.find();
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

    public String execute(String query, String outputValue) {
        StringBuilder output = new StringBuilder();
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);


            while (resultSet.next()) {
                output.append(resultSet.getString(outputValue));
            }

            logger.info(output.toString());
            return output.toString();


        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return output.toString();
    }

    public void update(String query) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
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

            String sql = "select * from account where username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();


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
            String sql = "select * from account where session_token = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, session);
            ResultSet resultSet = preparedStatement.executeQuery();


            Account account = null;
            while (resultSet.next()) {
                account = new Account();
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

            String sql = "select * from account where auth_token = \"?\"";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();


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
            if(findSpecialCharacter(username)) {registerJson.setSuccess(false); registerJson.setReason("illegal character in username"); return registerJson;}

            String checkUsernameSql = "select * from account where UPPER(?) = UPPER(?)";
            String newAccountSql = "insert into account (username, password, account_created, last_ip, last_login, account_type, chat_access, auth_token, session_token)" +
                    " values (?, ?, now(), ?, now(), ?, ?, ?, ?)";

            Random random = new Random();
            String auth = random.ints(97,123).limit(32).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            String session = random.ints(97,123).limit(128).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

            PreparedStatement preparedStatement = connection.prepareStatement(checkUsernameSql);
            ResultSet usernameExistsRs = preparedStatement.executeQuery();

            if(usernameExistsRs.next()) {
                registerJson.setSuccess(false);
                registerJson.setReason("username is already taken");
                return registerJson;
            }
            Statement statement = connection.createStatement();

            while (statement.executeQuery("select * from account where auth_token = '" + auth + "'").next()) {
                auth = random.ints(97,123).limit(32).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            }

            while (statement.executeQuery("select * from account where session_token = '" + session + "'").next()) {
                session = random.ints(97,123).limit(128).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            }

            byte[] salt = Utils.generateSalt();
            password = Utils.sha512(password, salt);
            password = Utils.byteToHex(salt) + ":" + password;

            logger.debug(String.valueOf(password.length()));

            preparedStatement = connection.prepareStatement(checkUsernameSql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, exchange.getRemoteAddress().getAddress().getHostAddress());
            preparedStatement.setInt(4, accountType);
            preparedStatement.setBoolean(5, true);
            preparedStatement.setString(6, auth);
            preparedStatement.setString(7, session);

            preparedStatement.executeQuery();

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

    public void updateLastIpAndDate(HttpExchange exchange, Account account) {

        try {

            PreparedStatement statement = connection.prepareStatement("update account as a set " +
                    "last_ip = ? ," +
                    "last_login = now() WHERE session_token = ?"
            );

            statement.setString(1, exchange.getRemoteAddress().getAddress().getHostAddress());
            statement.setString(2, account.getSession());
            statement.executeUpdate();



        } catch (Exception e) {
            logger.error(e.getMessage());
        }



    }
}