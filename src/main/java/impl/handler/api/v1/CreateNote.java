package impl.handler.api.v1;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.database.Account;
import impl.json.ConfigJson;
import impl.json.note.NoteConfigJson;
import impl.json.note.NoteCreationJson;
import impl.json.note.NoteResponseJson;
import impl.utils.Utils;
import impl.utils.finals.Global;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class CreateNote implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {



        NoteCreationJson noteCreationGson = Global.gson.fromJson(Utils.getFromPost(exchange), NoteCreationJson.class);
        NoteResponseJson responseJson = new NoteResponseJson();


        String session = Utils.getCurrentSession(exchange);

        if(session == null) {
            responseJson.setSuccess(false);
            responseJson.setRedirect("");
            responseJson.setReason("you are not logged in!");
            Utils.sendOutput(exchange, Global.gson.toJson(responseJson), false, 200);
            return;
        }

        Account account = Global.database.getAccountBySession(session);

        if(account == null) {
            responseJson.setSuccess(false);
            responseJson.setRedirect("");
            responseJson.setReason("expired session");
            Utils.sendOutput(exchange, Global.gson.toJson(responseJson), false, 200);
            return;
        }

        if(noteCreationGson == null) {
            responseJson.setSuccess(false);
            responseJson.setRedirect("");
            responseJson.setReason("failed to create note");
            Utils.sendOutput(exchange, Global.gson.toJson(responseJson), false, 200);
            return;
        }




        NoteConfigJson noteConfig = new NoteConfigJson();

        noteConfig.setBurnAfterRead(noteCreationGson.isBurnAfterRead());
        noteConfig.setAuthor(account.getUsername());
        noteConfig.setDate(new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime()));
        noteConfig.setPassword(noteCreationGson.getPassword());
        noteConfig.setPasswordProtected(noteConfig.isPasswordProtected());

        String name = UUID.randomUUID().toString();

        while (true) {
            boolean repeats = false;
            File directory = new File("notes");
            File[] noteList = directory.listFiles();

            if(noteList == null) {
                break;
            }

            for (File note : noteList) {
                if(note.isFile()) {
                    if(note.getName().equals(repeats + ".json")) {
                        repeats = true;
                        name = UUID.randomUUID().toString();
                    }
                }

            }

            if(!repeats) {
                break;
            }
        }


        noteConfig.setContentFile(name + ".txt");

        File configFile = new File("notes/" + name + ".json");
        File dataFile = new File("notes/content/" + name + ".txt");

        FileWriter configWriter = new FileWriter(configFile);
        FileWriter dataWriter = new FileWriter(dataFile);

        configWriter.write(Global.gson.toJson(noteConfig, NoteConfigJson.class));
        configWriter.close();

        dataWriter.write(noteCreationGson.getContent());
        dataWriter.close();

        responseJson.setSuccess(true);

        responseJson.setRedirect("/note-success?note=" + name);

        responseJson.setReason("");
        Utils.sendOutput(exchange, Global.gson.toJson(responseJson), false, 200);


    }
}
