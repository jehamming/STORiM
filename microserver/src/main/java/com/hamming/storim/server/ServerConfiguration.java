package com.hamming.storim.server;

import com.hamming.storim.server.common.model.BasicObject;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.common.model.TileSet;

public class ServerConfiguration extends BasicObject {
    private TileSet defaultTileSet;
    private int defaultTile;
    private Room defaultRoom;


    public TileSet getDefaultTileSet() {
        return defaultTileSet;
    }

    public void setDefaultTileSet(TileSet defaultTileSet) {
        this.defaultTileSet = defaultTileSet;
    }

    public Room getDefaultRoom() {
        return defaultRoom;
    }

    public void setDefaultRoom(Room defaultRoom) {
        this.defaultRoom = defaultRoom;
    }

    public int getDefaultTile() {
        return defaultTile;
    }

    public void setDefaultTile(int defaultTile) {
        this.defaultTile = defaultTile;
    }
}
