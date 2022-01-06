package com.hamming.storim.server.common.model;

import java.io.Serializable;

public class Location implements Serializable {

    private long sequence;
    private Room room;
    private int x,y;

    public Location(Room r, int x, int y) {
        this.room = r;
        this.x = x;
        this.y = y;
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

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }
}
