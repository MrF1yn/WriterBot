package dev.mrflyn.writerbot.apis.pasteggapi;

public class PasteFile {
    private String name;
    private PasteFileContent content;

    public PasteFile(String name, PasteFileContent content){
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PasteFileContent getContent() {
        return content;
    }

    public void setContent(PasteFileContent content) {
        this.content = content;
    }
}
