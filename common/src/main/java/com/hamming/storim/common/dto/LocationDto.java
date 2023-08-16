package com.hamming.storim.common.dto;

public class LocationDto extends BasicObjectDTO {
    // ID of Object that this location belongs to
    private Long objectId;
    // Which server the object is
    private String serverId;
    // The room the object is
    private Long roomId;
    private int x,y;

    public LocationDto( Long objectId, String serverId, Long roomId, int x, int y) {
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

    public Long getRoomId() {
        return roomId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "LocationDto{" +
                "objectId=" + objectId +
                ", serverId='" + serverId + '\'' +
                ", roomId=" + roomId +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
