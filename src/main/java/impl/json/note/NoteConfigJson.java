package impl.json.note;

import com.google.gson.annotations.SerializedName;

public class NoteConfigJson {
    @SerializedName("content-file")
    private String contentFile;

    @SerializedName("password-protected")
    private boolean passwordProtected;

    @SerializedName("password")
    private String password;

    @SerializedName("author")
    private String author;

    @SerializedName("creation-date")
    private String date;

    @SerializedName("burn-after-read")
    private boolean burnAfterRead;

    public String getContentFile() {
        return contentFile;
    }

    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isBurnAfterRead() {
        return burnAfterRead;
    }

    public void setBurnAfterRead(boolean burnAfterRead) {
        this.burnAfterRead = burnAfterRead;
    }

}
