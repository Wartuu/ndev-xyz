package impl.json;

import com.google.gson.annotations.SerializedName;

public class VersionJson {

    @SerializedName("main-version")
    private String mainVersion;

    @SerializedName("frontend-version")
    private String frontendVersion;

    @SerializedName("backend-version")
    private String backendVersion;


    public String getMainVersion() {
        return mainVersion;
    }

    public void setMainVersion(String mainVersion) {
        this.mainVersion = mainVersion;
    }

    public String getFrontendVersion() {
        return frontendVersion;
    }

    public void setFrontendVersion(String frontendVersion) {
        this.frontendVersion = frontendVersion;
    }

    public String getBackendVersion() {
        return backendVersion;
    }

    public void setBackendVersion(String backendVersion) {
        this.backendVersion = backendVersion;
    }
}
