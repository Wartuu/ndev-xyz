package impl.json;

import com.google.gson.annotations.SerializedName;

public class Config {
    @SerializedName("http-port")
    private int httpPort;
    @SerializedName("ws-port")
    private int websocketPort;

    @SerializedName("http-executor-thread-pool-overrun-size")
    private int executorOverrunSize;

    @SerializedName("http-executor-thread-pool-core-size")
    private int executorCoreSize;

    @SerializedName("http-executor-thread-pool-max-idle-time")
    private int executorMaxIdleTime;

    @SerializedName("http-executor-thread-pool-max-thread-work-queue")
    private int executorMaxWorkQueue;




    @SerializedName("db-url")
    private String databaseUrl;

    @SerializedName("db-username")
    private String databaseUsername;

    @SerializedName("db-password")
    private String databasePassword;


    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getWebsocketPort() {
        return websocketPort;
    }

    public void setWebsocketPort(int websocketPort) {
        this.websocketPort = websocketPort;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public int getExecutorOverrunSize() {
        return executorOverrunSize;
    }

    public void setExecutorOverrunSize(int executorOverrunSize) {
        this.executorOverrunSize = executorOverrunSize;
    }

    public int getExecutorCoreSize() {
        return executorCoreSize;
    }

    public void setExecutorCoreSize(int executorCoreSize) {
        this.executorCoreSize = executorCoreSize;
    }

    public int getExecutorMaxIdleTime() {
        return executorMaxIdleTime;
    }

    public void setExecutorMaxIdleTime(int executorMaxIdleTime) {
        this.executorMaxIdleTime = executorMaxIdleTime;
    }

    public int getExecutorMaxWorkQueue() {
        return executorMaxWorkQueue;
    }

    public void setExecutorMaxWorkQueue(int executorMaxWorkQueue) {
        this.executorMaxWorkQueue = executorMaxWorkQueue;
    }
}
