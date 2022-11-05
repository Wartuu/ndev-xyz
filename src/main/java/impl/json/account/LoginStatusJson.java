package impl.json.account;

import com.google.gson.annotations.SerializedName;

public class LoginStatusJson {
    @SerializedName("success")
    private boolean success;

    @SerializedName("reason")
    private String reason;

    @SerializedName("session")
    private String session;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
