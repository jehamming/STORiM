package com.hamming.storim.model;

// A baseplate is like a lego baseplate.
public class Room extends BasicObject   {

    private int size; //Tiles: size * size
    private String name;
    private int spawnPointX;
    private int spawnPointY;
    private Tile tile;

    public Room(Long id) {
        super(id);
        spawnPointX = 0;
        spawnPointY = 0;
        tile = null;
        name = "A basic room";
        size = 10;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public String toString() {
        return "Room{" +
                " id=" + getId() +
                ", size=" + size +
                ", name='" + name + '\'' +
                ", spawnPointX=" + spawnPointX +
                ", spawnPointY=" + spawnPointY +
                ", tile=" + tile +
                '}';
    }
}
