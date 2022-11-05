package impl.json;

import com.google.gson.annotations.SerializedName;

public class NoteConfigGson {
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

    @SerializedName("use-encryption-key")
    private boolean useEncryptionKey;



}
