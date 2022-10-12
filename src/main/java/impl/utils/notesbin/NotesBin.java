package impl.utils.notesbin;

import impl.utils.finals.Global;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NotesBin {


    private final Logger logger = LoggerFactory.getLogger(NotesBin.class);
    private OkHttpClient httpClient = null;
    private NotesBinMapping notesBinMapping = null;
    private String url;
    private String id;
    private String authToken;


    public NotesBin(String url, String id, String authToken) {
        this.httpClient = new OkHttpClient();
        this.url = url;
        this.id = id;
        this.authToken = authToken;

        Request mappingRequest = new Request.Builder()
                .url(url + "/api/v2/mappings")
                .addHeader("Authorization", authToken).build();

        Call mappingRequestCall = httpClient.newCall(mappingRequest);

        try {
            Response response = mappingRequestCall.execute();

            if(response.code() == 200) {
                String resp = response.body().string();
                notesBinMapping = Global.gson.fromJson(resp, NotesBinMapping.class);
                logger.info(resp);
                logger.info("connecting to " + url + " successfully");

            } else {
                logger.error("getting NotesBin mapping FAILED from url => " + url + "/api/v2/mappings");
                logger.error("response code was: " + response.code());
                return;
            }
        } catch (IOException e) {logger.error(e.getMessage()); logger.error("failed connecting to NotesBin at url: " + this.url);}
    }

    public String getAuthUrl() {
        return "https://" + url + "/api/v2/oAuthAuthorize?appid=" + id + "&redirect=https://nekodev.xyz/callback";
    }

    public NotesBinUserJson getUserInfo(String token) {
        try {
            String responseString;

            Request request;
            request = new Request.Builder()
                    .url(url + "/api/v2/oAuthGetUserInfo" + "?token=" + token)
                    .build();

            Call call = httpClient.newCall(request);

            Response response = call.execute();

            if(response.code() == 200 ) {
                responseString = response.body().string();
                return Global.gson.fromJson(responseString, NotesBinUserJson.class);
            }
        } catch (IOException e) {logger.error(e.getMessage());}

         return null;
    }



}
