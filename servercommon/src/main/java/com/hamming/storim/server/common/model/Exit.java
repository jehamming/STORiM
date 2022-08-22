package com.hamming.storim.server.common.model;

import com.hamming.storim.server.common.ImageObject;

public class Exit extends ImageObject {

    private Long toRoomID;
    private String toServerID;
    private String description = "";
    private float scale;
    private int rotation;
    private int x,y;

    public Exit(String name, Long toRoomID, String toServerID) {
        setName(name);
        this.toRoomID = toRoomID;
        this.toServerID = toServerID;
    }

    public Long getToRoomID() {
        return toRoomID;
    }

    public void setToRoomID(Long toRoomID) {
        this.toRoomID = toRoomID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getToServerID() {
        return toServerID;
    }

    public void setToServerID(String toServerID) {
        this.toServerID = toServerID;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Exit{" +
                "toRoomID=" + toRoomID +
                ", toServerID='" + toServerID + '\'' +
                ", description='" + description + '\'' +
                ", scale=" + scale +
                ", rotation=" + rotation +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}


