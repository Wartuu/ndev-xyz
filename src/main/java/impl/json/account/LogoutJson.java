package impl.json.account;

import com.google.gson.annotations.SerializedName;

public class LogoutJson {
    @SerializedName("success")
    private boolean success;

    @SerializedName("reason")
    private String reason;

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
}
