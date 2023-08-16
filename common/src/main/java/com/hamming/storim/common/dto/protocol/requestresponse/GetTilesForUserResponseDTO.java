package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.List;
import java.util.Map;

public class GetTilesForUserResponseDTO extends ResponseDTO {

    private final List<Long> tiles;

    public GetTilesForUserResponseDTO(boolean success, List<Long> tiles, String errorMessage) {
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
