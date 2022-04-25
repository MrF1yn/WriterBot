package dev.mrflyn.writerbot.pasteapi;

import java.util.List;

public class Paste {
    private String name;
    private String description;
    private String visibility;
//    private String expires;
    private List<PasteFile> files;

    public Paste(String name, String description, String visibility, List<PasteFile> files){
        this.name = name;
        this.description = description;
        this.visibility = visibility;
//        this.expires = expires;
        this.files = files;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

//    public String getExpires() {
//        return expires;
//    }
//
//    public void setExpires(String expires) {
//        this.expires = expires;
//    }

    public List<PasteFile> getFiles() {
        return files;
    }

    public void setFiles(List<PasteFile> files) {
        this.files = files;
    }
}
