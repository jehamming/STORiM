package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.List;
import java.util.Map;

public class GetTilesForUserResponseDTO implements  ResponseDTO {

    private final List<Long> tiles;

    public GetTilesForUserResponseDTO(List<Long> tiles) {
        this.tiles = tiles;
    }

    public List<Long> getTiles() {
        return tiles;
    }

    @Override
    public String toString() {
        return "GetTilesForUserResponseDTO{" +
                "tiles=" + tiles +
                '}';
    }
}
