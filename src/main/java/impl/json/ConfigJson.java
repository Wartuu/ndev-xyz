package impl.json;

import com.google.gson.annotations.SerializedName;

public class ConfigJson {
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

    @SerializedName("backend-version")
    private String backendVersion;

    @SerializedName("frontend-version")
    private String frontendVersion;

    @SerializedName("main-version")
    private String mainVersion;

    @SerializedName("http-executor-worker-priority")
    private int executorWorkerPriority;

    @SerializedName("server-domain")
    private String serverDomain;

    public int getHttpPort() {
        return httpPort;
    }

    public int getWebsocketPort() {
        return websocketPort;
    }

    public int getExecutorOverrunSize() {
        return executorOverrunSize;
    }

    public int getExecutorCoreSize() {
        return executorCoreSize;
    }

    public int getExecutorMaxIdleTime() {
        return executorMaxIdleTime;
    }

    public int getExecutorMaxWorkQueue() {
        return executorMaxWorkQueue;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public String getBackendVersion() {
        return backendVersion;
    }

    public String getFrontendVersion() {
        return frontendVersion;
    }

    public String getMainVersion() {
        return mainVersion;
    }

    public int getExecutorWorkerPriority() {
        return executorWorkerPriority;
    }

    public String getServerDomain() {
        return serverDomain;
    }
}
