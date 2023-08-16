package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetTileResponseDTO extends ResponseDTO {

    private TileDto tile;

    public GetTileResponseDTO(boolean success, String errorMessage, TileDto tile) {
        super(success, errorMessage);
        this.tile = tile;
    }

    public TileDto getTile() {
        return tile;
    }

    @Override
    public String toString() {
        return "GetTileResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", tile=" + tile +
                '}';
    }
}
