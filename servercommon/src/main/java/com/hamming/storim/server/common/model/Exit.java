package com.hamming.storim.server.common.model;

public class Exit extends BasicObject   {

    public static enum Orientation { NORTH, SOUTH, EAST, WEST };

    private Orientation orientation;
    private Long roomid;

    public Exit(String name, Orientation orientation, Long roomid) {
        setName(name);
        this.orientation = orientation;
        this.roomid = roomid;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Long getRoomid() {
        return roomid;
    }

    public void setRoomid(Long roomid) {
        this.roomid = roomid;
    }

    @Override
    public String toString() {
        return "Exit{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", orientation=" + orientation +
                ", roomid=" + roomid +
                '}';
    }
}


