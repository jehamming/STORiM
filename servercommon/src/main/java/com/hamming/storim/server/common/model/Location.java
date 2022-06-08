package com.hamming.storim.server.common.model;

import java.io.Serializable;

public class Location extends BasicObject {

    // ID of Object that this location belongs to
    private Long objectId;
    // Which server the object is
    private String serverId;
    // The room the object is
    private Long roomId;
    private int x,y;

    public Location(Long objectId, String serverId, Long roomId, int x, int y) {
        this.objectId = objectId;
        this.serverId = serverId;
        this.roomId = roomId;
        this.x = x;
        this.y = y;
    }

    public Long getObjectId() {
        return objectId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
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
}
