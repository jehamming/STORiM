package com.hamming.storim.server.game;

import com.hamming.storim.common.dto.DTO;

public class ServerEvent {

    public enum Type {
        USERCONNECTED,
        USERDISCONNECTED,
    }

    private Type type;
    private DTO data;

    public ServerEvent(Type type, DTO data) {
        this.type = type;
        this.data = data;
    }

    public Type getType() {
        return type;
    }

    public DTO getData() {
        return data;
    }
}
