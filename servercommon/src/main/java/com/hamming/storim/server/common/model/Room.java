package com.hamming.storim.server.common.model;

import com.hamming.storim.common.dto.ThingDto;

import java.util.ArrayList;
import java.util.List;

public class Room extends BasicObject   {

    private int rows, cols;
    private int length, width;
    private int spawnPointX;
    private int spawnPointY;
    private Long tileId;

    private List<Exit> exits;
    private List<Long> objectsInRoom;

    public Room() {
        spawnPointX = 100;
        spawnPointY = 100;
        tileId = null;
        setName("A basic room");
        rows = 10;
        cols = 10;
        length = 200;
        width = 200;
        exits = new ArrayList<>();
        objectsInRoom = new ArrayList<>();
    }

    public int getSpawnPointX() {
        return spawnPointX;
    }

    public void setSpawnPointX(int spawnPointX) {
        this.spawnPointX = spawnPointX;
    }

    public int getSpawnPointY() {
        return spawnPointY;
    }

    public void setSpawnPointY(int spawnPointY) {
        this.spawnPointY = spawnPointY;
    }

    public Long getTileId() {
        return tileId;
    }

    public void setTileId(Long tileId) {
        this.tileId = tileId;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void addExit(Exit exit) {
        exits.add(exit);
    }

    public void removeExit(Exit exit) {
        exits.remove(exit);
    }

    public List<Exit> getExits() {
        return exits;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + getId() +
                ", rows=" + rows +
                ", cols=" + cols +
                ", length=" + length +
                ", width=" + width +
                ", name='" + getName() + '\'' +
                ", spawnPointX=" + spawnPointX +
                ", spawnPointY=" + spawnPointY +
                ", tileId=" + tileId +
                '}';
    }

    public Exit getExit(Long exitID) {
        Exit found = null;
        for (Exit exit: exits) {
            if ( exit.getId().equals( exitID)) {
                found = exit;
                break;
            }
        }
        return found;
    }

    public List<Long> getObjectsInRoom() {
        return objectsInRoom;
    }

    public void addObjectInRoom(Long objectId) {
        objectsInRoom.add(objectId);
    }
    public void removeObjectFromRoom(Long objectId) {
        objectsInRoom.remove(objectId);
    }

}
