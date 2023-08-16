package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class AddTileResponseDTO extends ResponseDTO {

    private TileDto tile;

    public AddTileResponseDTO(boolean success, String errorMessage, TileDto tile) {
        super(success, errorMessage);
        this.tile = tile;
    }

    public TileDto getTile() {
        return tile;
    }

    @Override
    public String toString() {
        return "AddTileResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", tile=" + tile +
                '}';
    }
}
