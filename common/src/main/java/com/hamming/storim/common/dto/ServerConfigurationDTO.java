package com.hamming.storim.common.dto;

import java.util.List;

public class ServerConfigurationDTO extends BasicObjectDTO {

    private String serverName;
    private Long defaultTileSetId;
    private int defaultTile;
    private Long defaultRoomId;
    private List<Long> serverAdmins;


    public ServerConfigurationDTO(String serverName, Long defaultTileSetId, int defaultTile, Long defaultRoomId, List<Long> serverAdmins) {
        this.serverName = serverName;
        this.defaultTileSetId = defaultTileSetId;
        this.defaultTile = defaultTile;
        this.defaultRoomId = defaultRoomId;
        this.serverAdmins = serverAdmins;
    }

    public String getServerName() {
        return serverName;
    }

    public Long getDefaultTileSetId() {
        return defaultTileSetId;
    }

    public int getDefaultTile() {
        return defaultTile;
    }

    public Long getDefaultRoomId() {
        return defaultRoomId;
    }

    public List<Long> getServerAdmins() {
        return serverAdmins;
    }

    @Override
    public String toString() {
        return "ServerConfigurationDTO{" +
                "serverName='" + serverName + '\'' +
                ", defaultTileSetId=" + defaultTileSetId +
                ", defaultTile=" + defaultTile +
                ", defaultRoomId=" + defaultRoomId +
                ", serverAdmins=" + serverAdmins +
                '}';
    }
}
