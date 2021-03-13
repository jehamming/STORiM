package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.TileDto;

public class GetTileResultDTO implements ProtocolDTO {

    private boolean success = false;
    private String errorMessage;
    private TileDto tile;

    public GetTileResultDTO(boolean success, String errorMessage, TileDto tile) {
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
        return "GetTileResultDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", tile=" + tile +
                '}';
    }
}
