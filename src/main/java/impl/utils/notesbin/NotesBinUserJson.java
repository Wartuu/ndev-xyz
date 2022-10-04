package impl.utils.notesbin;

public class NotesBinUserJson {
    private NotesBinUser user;
    private NotesBinUserApp userApp;


    public static final class NotesBinUserApp {
        private final String name, description, avatarURL, id;

        public NotesBinUserApp(String name, String description, String avatarURL, String id) {
            this.name = name;
            this.description = description;
            this.avatarURL = avatarURL;
            this.id = id;
        }
    }

    public static final class NotesBinUser {
        private final String name, id;
        private final boolean admin, premium;
        private final String roleColor, roleName;

        public NotesBinUser(String name, String id, boolean admin, boolean premium, String roleColor, String roleName) {
            this.name = name;
            this.id = id;
            this.admin = admin;
            this.premium = premium;
            this.roleColor = roleColor;
            this.roleName = roleName;
        }
    }
}