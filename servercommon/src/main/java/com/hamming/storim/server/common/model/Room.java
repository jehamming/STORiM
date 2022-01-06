package com.hamming.storim.server.common.model;

// A baseplate is like a lego baseplate.
public class Room extends BasicObject   {

    private int rows, cols;
    private int length, width;
    private String name;
    private int spawnPointX;
    private int spawnPointY;
    private Long tileId;

    public Room() {
        spawnPointX = 0;
        spawnPointY = 0;
        tileId = null;
        name = "A basic room";
        rows = 10;
        cols = 10;
        length = 200;
        width = 200;
    }

    public int getSpawnPointX() {
        return spawnPointX;
    }

    public void setSpawnPointX(int spawnPointX) {
        this.spawnPointX = spawnPointX;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Room{" +
                "rows=" + rows +
                ", cols=" + cols +
                ", length=" + length +
                ", width=" + width +
                ", name='" + name + '\'' +
                ", spawnPointX=" + spawnPointX +
                ", spawnPointY=" + spawnPointY +
                ", tileId=" + tileId +
                '}';
    }
}
