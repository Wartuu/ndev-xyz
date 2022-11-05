package impl.json.note;

import com.google.gson.annotations.SerializedName;

public class NoteResponseJson {
    @SerializedName("success")
    private boolean success;

    @SerializedName("reason")
    private String reason;

    @SerializedName("redirect")
    private String redirect;



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

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

}
