package impl.json;

import com.google.gson.annotations.SerializedName;

public class ConfigJson {
    @SerializedName("http-port")
    private int httpPort;
    @SerializedName("ws-port")
    private int websocketPort;

    @SerializedName("add-port-to-redirection")
    private boolean addPortToRedirection;

    @SerializedName("http-executor-thread-pool-max-size")
    private int executorOverrunSize;

    @SerializedName("http-executor-thread-pool-core-size")
    private int executorCoreSize;

    @SerializedName("http-executor-thread-pool-max-idle-time")
    private int executorMaxIdleTime;

    @SerializedName("db-url")
    private String databaseUrl;

    @SerializedName("db-username")
    private String databaseUsername;

    @SerializedName("db-password")
    private String databasePassword;

    @SerializedName("graalvm-logfile")
    private String graalvmLogFile;

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

    @SerializedName("NotesBin-id")
    private String notesBinId;

    @SerializedName("NotesBin-url")
    private String notesBinUrl;

    @SerializedName("NotesBin-auth")
    private String notesBinAuth;

    @SerializedName("NotesBin-support")
    private boolean notesBinSupport;

    @SerializedName("request-meter-delay")
    private int requestMeterDelay;

    @SerializedName("mail-host")
    private String mailHost;

    @SerializedName("mail-port")
    private int mailPort;

    @SerializedName("mail-username")
    private String mailUsername;

    @SerializedName("mail-password")
    private String mailPassword;

    @SerializedName("mail-auth")
    private boolean mailAuth;

    @SerializedName("mail-tls")
    private boolean mailTls;

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


    public String getNotesBinId() {
        return notesBinId;
    }

    public String getNotesBinUrl() {
        return notesBinUrl;
    }

    public String getNotesBinAuth() {
        return notesBinAuth;
    }

    public boolean isNotesBinSupport() {
        return notesBinSupport;
    }

    public int getRequestMeterDelay() {
        return requestMeterDelay;
    }

    public String getMailHost() {
        return mailHost;
    }

    public int getMailPort() {
        return mailPort;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public boolean isMailAuth() {
        return mailAuth;
    }

    public boolean isMailTls() {
        return mailTls;
    }

    public String getGraalvmLogFile() {
        return graalvmLogFile;
    }

    public boolean isAddPortToRedirection() {
        return addPortToRedirection;
    }
}
