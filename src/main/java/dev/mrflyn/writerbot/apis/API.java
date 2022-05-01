package dev.mrflyn.writerbot.apis;

import dev.mrflyn.writerbot.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum API {
    HASTE_BIN(new HasteBin()),
    HASTE_BIN_MD5(new HasteBinMD5()),
    HELP_CHAT(new HelpChat()),
    PASTE_GG(new PasteGG());

    private final ApiInterface wrapper;

    private API(ApiInterface wrapper){
        this.wrapper = wrapper;
    }

    public ApiInterface getWrapper() {
        return this.wrapper;
    }

    public static List<String> getAllApis(){
        return Main.allAPIs;
    }
}
