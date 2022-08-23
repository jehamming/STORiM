package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetTileRequestDTO extends ProtocolDTO {
    private Long tileId;
    public GetTileRequestDTO(Long tileId) {
        this.tileId = tileId;
    }

    public Long getTileId() {
        return tileId;
    }

    @Override
    public String toString() {
        return "GetTileRequestDTO{" +
                "tileId=" + tileId +
                '}';
    }
}
