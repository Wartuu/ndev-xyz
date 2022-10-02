package impl.utils.executor.notesbin;

import com.google.gson.annotations.SerializedName;

public class NotesBinMapping {
    @SerializedName("user.delete")
    private String userDelete;

    @SerializedName("admin.createwebhook")
    private String adminCreateWebhook;

    @SerializedName("admin.webhook")
    private String adminWebhook;

    @SerializedName("public.login")
    private String publicLogin;

    @SerializedName("user.stats")
    private String userStats;

    @SerializedName("admin.updatechangelog")
    private String adminUpdateChangeLog;

    @SerializedName("user.ircstats")
    private String userIrcStats;

    @SerializedName("user.upload")
    private String userUpload;

    @SerializedName("admin.createinvite")
    private String adminCreateInvite;

    @SerializedName("admin.noteinfo")
    private String adminNoteInfo;

    @SerializedName("admin.notesbinstats")
    private String adminNotesBinStats;

    @SerializedName("user.ircip")
    private String userIrcIp;

    @SerializedName("user.adddatatofeed")
    private String userAddDataToFeed;

    @SerializedName("admin.webhookinfo")
    private String adminWebhookInfo;

    @SerializedName("public.version")
    private String publicVersion;

    @SerializedName("admin.userlookup")
    private String adminUserLookup;

    @SerializedName("user.createoauthtoken")
    private String userCreateOauthToken;

    @SerializedName("user.oauthgetuserinfo")
    private String userOAuthGetUserInfo;

    @SerializedName("user.userauthorizedoauthapps")
    private String userAuthorizedAuthApps;

    @SerializedName("user.userauthorizedoauthappsrevoketoken")
    private String userAuthorizedAuthAppsRevokeToken;

    @SerializedName("user.profilepreferencesupdate")
    private String userProfilePreferencesUpdate;


    public String getUserDelete() {
        return userDelete;
    }

    public String getAdminCreateWebhook() {
        return adminCreateWebhook;
    }

    public String getAdminWebhook() {
        return adminWebhook;
    }

    public String getPublicLogin() {
        return publicLogin;
    }

    public String getUserStats() {
        return userStats;
    }

    public String getAdminUpdateChangeLog() {
        return adminUpdateChangeLog;
    }

    public String getUserIrcStats() {
        return userIrcStats;
    }

    public String getUserUpload() {
        return userUpload;
    }

    public String getAdminCreateInvite() {
        return adminCreateInvite;
    }

    public String getAdminNoteInfo() {
        return adminNoteInfo;
    }

    public String getAdminNotesBinStats() {
        return adminNotesBinStats;
    }

    public String getUserIrcIp() {
        return userIrcIp;
    }

    public String getUserAddDataToFeed() {
        return userAddDataToFeed;
    }

    public String getAdminWebhookInfo() {
        return adminWebhookInfo;
    }

    public String getPublicVersion() {
        return publicVersion;
    }

    public String getAdminUserLookup() {
        return adminUserLookup;
    }

    public String getUserCreateOauthToken() {
        return userCreateOauthToken;
    }

    public String getUserOAuthGetUserInfo() {
        return userOAuthGetUserInfo;
    }

    public String getUserAuthorizedAuthApps() {
        return userAuthorizedAuthApps;
    }

    public String getUserAuthorizedAuthAppsRevokeToken() {
        return userAuthorizedAuthAppsRevokeToken;
    }

    public String getUserProfilePreferencesUpdate() {
        return userProfilePreferencesUpdate;
    }


}
