package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class AddTileResponseDTO implements ResponseDTO {

    private boolean success;
    private String errorMessage;
    private TileDto tile;

    public AddTileResponseDTO(boolean success, String errorMessage, TileDto tile) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.tile = tile;
    }

    public boolean isSuccess() {
        return success;
    }

    public TileDto getTile() {
        return tile;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "AddTileResponseDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", tile=" + tile +
                '}';
    }
}
