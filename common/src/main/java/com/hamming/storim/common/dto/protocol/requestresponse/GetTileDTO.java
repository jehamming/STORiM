package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetTileDTO implements ProtocolDTO {
    private Long tileId;
    public GetTileDTO(Long tileId) {
        this.tileId = tileId;
    }

    public Long getTileId() {
        return tileId;
    }

    @Override
    public String toString() {
        return "GetTileDTO{" +
                "tileId=" + tileId +
                '}';
    }
}
