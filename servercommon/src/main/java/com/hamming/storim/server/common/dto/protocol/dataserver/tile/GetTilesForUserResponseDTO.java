package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.List;

public class GetTilesForUserResponseDTO extends ResponseDTO {

    private boolean success = false;
    private String errorMessage;
    private final List<Long> tiles;

    public GetTilesForUserResponseDTO(boolean success, String errorMessage, List<Long> tiles) {
        this.tiles = tiles;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public List<Long> getTiles() {
        return tiles;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "GetTilesForUserResponseDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", tiles=" + tiles +
                '}';
    }
}
