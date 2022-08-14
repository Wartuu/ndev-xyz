package impl.database;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;


public class Account {

    @SerializedName("account-id")
    private long id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("account-creation-date")
    private Date accountCreated;

    @SerializedName("last-ip")
    private String lastIp;

    @SerializedName("last-login")
    private Date lastLogin;

    @SerializedName("account-type")
    private int accountType;
    @SerializedName("chat-access")
    private boolean chatAccess;

    @SerializedName("authorization-token")
    private String authToken;

    @SerializedName("session-token")
    private String session;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getAccountCreated() {
        return accountCreated;
    }

    public void setAccountCreated(Date accountCreated) {
        this.accountCreated = accountCreated;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public boolean isChatAccess() {
        return chatAccess;
    }

    public void setChatAccess(boolean chatAccess) {
        this.chatAccess = chatAccess;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
