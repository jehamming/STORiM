package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetTileResponseDTO implements ResponseDTO {

    private boolean success = false;
    private String errorMessage;
    private TileDto tile;

    public GetTileResponseDTO(boolean success, String errorMessage, TileDto tile) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.tile = tile;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public TileDto getTile() {
        return tile;
    }

    @Override
    public String toString() {
        return "GetTileResponseDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", tile=" + tile +
                '}';
    }
}
