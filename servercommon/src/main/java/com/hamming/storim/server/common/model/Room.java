package com.hamming.storim.server.common.model;

import com.hamming.storim.common.dto.ThingDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Room extends BasicObject   {

    private int rows, cols;
    private Long tileSetId;
    private int[][] tileMap;
    private int spawnPointX;
    private int spawnPointY;
    private List<Exit> exits;
    private List<Long> objectsInRoom;

    public Room() {
        spawnPointX = 100;
        spawnPointY = 100;
        tileSetId = null;
        setName("A basic room");
        rows = 10;
        cols = 10;
        createTileMap();
        exits = new ArrayList<>();
        objectsInRoom = new ArrayList<>();
    }

    private void createTileMap() {
        tileMap = new int[cols][rows];
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                tileMap[c][r] = -1;
            }
        }
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

    public void setTileSetId(Long tileSetId) {
        this.tileSetId = tileSetId;
    }

    public Long getTileSetId() {
        return tileSetId;
    }

    public int[][] getTileMap() {
        return tileMap;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
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

    public void setTileMap(int[][] tileMap) {
        this.tileMap = tileMap;
    }

    @Override
    public String toString() {
        return "Room{" +
                "rows=" + rows +
                ", cols=" + cols +
                ", tileSetId=" + tileSetId +
                ", tileMap=" + Arrays.toString(tileMap) +
                ", spawnPointX=" + spawnPointX +
                ", spawnPointY=" + spawnPointY +
                ", exits=" + exits +
                ", objectsInRoom=" + objectsInRoom +
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

    public void setSize(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        createTileMap();
    }

}
