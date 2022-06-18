package com.hamming.storim.server.game;

import com.hamming.storim.common.dto.DTO;

public class RoomEvent {

    public enum Type {
        USERUPDATED,
        USERLEFTROOM,
        USERENTEREDROOM,
        USERLOCATIONUPDATE,
        AVATARSET,
        THINGPLACED,
        THINGLOCATIONUPDATE,
        THINGUPDATED,
        ROOMUPDATED,
        MESSAGEINROOM
    }

    private Type type;
    private DTO data;
    private Object extraData;

    public RoomEvent(Type type, DTO data) {
        this.type = type;
        this.data = data;
    }

    public RoomEvent(Type type, DTO data, Object extraData) {
        this(type, data);
        this.extraData = extraData;
    }

    public Type getType() {
        return type;
    }

    public DTO getData() {
        return data;
    }

    public Object getExtraData() {
        return extraData;
    }
}
