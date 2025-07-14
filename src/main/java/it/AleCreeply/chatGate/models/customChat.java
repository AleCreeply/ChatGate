package it.AleCreeply.chatGate.models;

public class customChat {

    private final String id;
    private final String format;
    private final String displayName;

    public customChat(String id, String format, String displayName) {
        this.id = id;
        this.format = format;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public String getFormat(){
        return format;
    }

    public String getDisplayName() {
        return displayName;
    }
}
