package dev.mrflyn.writerbot.pasteapi;

public class PasteFileContent {
    private String format;
    private String highlight_language;
    private String value;

    public PasteFileContent(String format, String highlight_language, String value){
        this.format = format;
        this.highlight_language = highlight_language;
        this.value = value;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getHighlight_language() {
        return highlight_language;
    }

    public void setHighlight_language(String highlight_language) {
        this.highlight_language = highlight_language;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
