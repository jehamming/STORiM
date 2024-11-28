package com.hamming.storim.common.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public abstract class JSONProtocolTransformer {

    private Gson gson;

    public static String COMMAND = "command";

    public enum commands {
        LOGIN
    }

    public JSONProtocolTransformer(Gson gson) {
        this.gson = gson;
    }

    public Gson getGson() {
        return gson;
    }

    public abstract ProtocolDTO transform(JsonObject jsonObject);
}
