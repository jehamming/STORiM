package com.hamming.storim.server;

import com.hamming.storim.server.common.model.BasicObject;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.common.model.TileSet;

import java.util.ArrayList;
import java.util.List;

public class ServerConfiguration extends BasicObject {
    private TileSet defaultTileSet;
    private int defaultTile;
    private Room defaultRoom;
    private List<Long> serverAdmins = new ArrayList<>();
    private Long superAdmin = null;
    private String serverName;



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

    public List<Long> getServerAdmins() {
        return serverAdmins;
    }

    public void setServerAdmins(List<Long> serverAdmins) {
        this.serverAdmins = serverAdmins;
    }

    public Long getSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(Long superAdmin) {
        this.superAdmin = superAdmin;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
