package com.hamming.storim.common.net.transformers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginDTO;
import com.hamming.storim.common.net.JSONProtocolTransformer;

public class LoginTransformer extends JSONProtocolTransformer {
    public LoginTransformer(Gson gson) {
        super(gson);
    }

    @Override
    public ProtocolDTO transform(JsonObject jsonObject) {
        String username = null, password = null;
        Long roomId = null;
        if (jsonObject.has("username")) username = jsonObject.get("username").getAsString();
        if (jsonObject.has("password")) password = jsonObject.get("password").getAsString();
        if (jsonObject.has("roomId")) roomId = jsonObject.get("roomId").getAsLong();

        LoginDTO dto = new LoginDTO(username, password, roomId);
        return dto;
    }
}
