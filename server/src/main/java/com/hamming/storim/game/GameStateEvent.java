package com.hamming.storim.game;

import com.hamming.storim.model.BasicObject;

public class GameStateEvent {

    public enum Type {
        USERCONNECTED,
        USERDISCONNECTED,
        USERTELEPORTED,
        USERLOCATION,
        VERBEXECUTED,
        VERBDELETED,
        ROOMDELETED
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
