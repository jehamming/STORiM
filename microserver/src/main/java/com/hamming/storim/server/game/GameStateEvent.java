package com.hamming.storim.server.game;

import com.hamming.storim.server.common.model.BasicObject;

public class GameStateEvent {

    public enum Type {
        USERCONNECTED,
        USERDISCONNECTED,
        USERUPDATED,
        USERLEFTROOM,
        USERLOCATION,
        VERBDELETED,
        AVATARADDED,
        AVATARUPDATED,
        AVATARDELETED,
        THINGADDED,
        THINGDELETED,
        THINGPLACED,
        THINGUPDATED,
        ROOMADDED,
        ROOMUPDATED,
        ROOMDELETED,
        MSGINROOM
    }

    private BasicObject object;
    private Type type;
    private Object extraData;

    public GameStateEvent(Type type, BasicObject object, Object extraData) {
        this.type = type;
        this.object = object;
        this.extraData = extraData;
    }


    public BasicObject getObject() {
        return object;
    }

    public Type getType() {
        return type;
    }

    public Object getExtraData() {
        return extraData;
    }
}
