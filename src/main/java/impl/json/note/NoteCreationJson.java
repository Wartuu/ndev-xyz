package impl.json.note;

import com.google.gson.annotations.SerializedName;

public class NoteCreationJson {
    @SerializedName("content")
    private String content;

    @SerializedName("password-protected")
    private boolean passwordProtected;

    @SerializedName("password")
    private String password;

    @SerializedName("burn-after-read")
    private boolean burnAfterRead;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBurnAfterRead() {
        return burnAfterRead;
    }

    public void setBurnAfterRead(boolean burnAfterRead) {
        this.burnAfterRead = burnAfterRead;
    }

}
