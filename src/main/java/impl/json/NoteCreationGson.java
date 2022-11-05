package impl.json;

import com.google.gson.annotations.SerializedName;

public class NoteCreationGson {
    @SerializedName("content")
    private String content;

    @SerializedName("password-protected")
    private boolean passwordProtected;

    @SerializedName("password")
    private boolean password;

    @SerializedName("burn-after-read")
    private boolean burnAfterRead;

    @SerializedName("use-encryption-key")
    private boolean useEncryptionKey;
}
