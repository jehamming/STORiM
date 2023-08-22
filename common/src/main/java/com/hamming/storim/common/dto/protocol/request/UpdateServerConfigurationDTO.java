package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.BasicObjectDTO;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.util.List;

public class UpdateServerConfigurationDTO extends ProtocolDTO {

    private Long defaultTileSetId;
    private int defaultTile;
    private Long defaultRoomId;
    private List<Long> serverAdmins;


    public UpdateServerConfigurationDTO(Long defaultTileSetId, int defaultTile, Long defaultRoomId, List<Long> serverAdmins) {
        this.defaultTileSetId = defaultTileSetId;
        this.defaultTile = defaultTile;
        this.defaultRoomId = defaultRoomId;
        this.serverAdmins = serverAdmins;
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
        return "UpdateServerConfigurationDTO{" +
                "defaultTileSetId=" + defaultTileSetId +
                ", defaultTile=" + defaultTile +
                ", defaultRoomId=" + defaultRoomId +
                ", serverAdmins=" + serverAdmins +
                '}';
    }
}
