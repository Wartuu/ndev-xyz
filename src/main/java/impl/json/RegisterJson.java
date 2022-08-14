package impl.json;

import com.google.gson.annotations.SerializedName;

public class RegisterJson {
    @SerializedName("auth-token")
    private String authToken;

    @SerializedName("session-token")
    private String sessionToken;


    @SerializedName("reason")
    private String reason;

    @SerializedName("success")
    private boolean success;


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
