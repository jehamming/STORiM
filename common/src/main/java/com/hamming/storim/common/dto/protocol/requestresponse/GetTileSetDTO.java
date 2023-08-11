package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetTileSetDTO extends ProtocolDTO {
    private Long tileSetId;
    public GetTileSetDTO(Long tileId) {
        this.tileSetId = tileId;
    }

    public Long getTileSetId() {
        return tileSetId;
    }

    @Override
    public String toString() {
        return "GetTileSetDTO{" +
                "tileSetId=" + tileSetId +
                '}';
    }
}
