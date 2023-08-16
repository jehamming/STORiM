package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.List;

public class GetTilesForUserResponseDTO extends ResponseDTO {
    private final List<Long> tiles;

    public GetTilesForUserResponseDTO(boolean success, String errorMessage, List<Long> tiles) {
        super(success, errorMessage);
        this.tiles = tiles;
    }

    public List<Long> getTiles() {
        return tiles;
    }

    @Override
    public String toString() {
        return "GetTilesForUserResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", tiles=" + tiles +
                '}';
    }
}
