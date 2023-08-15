package com.hamming.storim.server.common.model;

import com.hamming.storim.common.dto.ThingDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Room extends BasicObject   {

    private int rows, cols;
    private Long backTileSetId;
    private Long frontTileSetId;
    private int[][] backTileMap;
    private int[][] frontTileMap;
    private int spawnPointX;
    private int spawnPointY;
    private List<Exit> exits;
    private List<Long> objectsInRoom;

    public Room() {
        spawnPointX = 100;
        spawnPointY = 100;
        backTileSetId = null;
        frontTileSetId = null;
        setName("A basic room");
        rows = 10;
        cols = 10;
        createTileMaps();
        exits = new ArrayList<>();
        objectsInRoom = new ArrayList<>();
    }

    private void createTileMaps() {
        backTileMap = createEmptyTileMap();
        frontTileMap = createEmptyTileMap();
    }

    private int[][] createEmptyTileMap() {
        int [][] tileMap = new int[cols][rows];
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                tileMap[c][r] = -1;
            }
        }
        return tileMap;
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

    public Long getBackTileSetId() {
        return backTileSetId;
    }

    public void setBackTileSetId(Long backTileSetId) {
        this.backTileSetId = backTileSetId;
    }

    public Long getFrontTileSetId() {
        return frontTileSetId;
    }

    public void setFrontTileSetId(Long frontTileSetId) {
        this.frontTileSetId = frontTileSetId;
    }

    public int[][] getBackTileMap() {
        return backTileMap;
    }

    public void setBackTileMap(int[][] backTileMap) {
        this.backTileMap = backTileMap;
    }

    public int[][] getFrontTileMap() {
        return frontTileMap;
    }

    public void setFrontTileMap(int[][] frontTileMap) {
        this.frontTileMap = frontTileMap;
    }

    @Override
    public String toString() {
        return "Room{" +
                "rows=" + rows +
                ", cols=" + cols +
                ", backTileSetId=" + backTileSetId +
                ", frontTileSetId=" + frontTileSetId +
                ", backTileMap=" + Arrays.toString(backTileMap) +
                ", fronTileMap=" + Arrays.toString(frontTileMap) +
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
        createTileMaps();
    }

}
