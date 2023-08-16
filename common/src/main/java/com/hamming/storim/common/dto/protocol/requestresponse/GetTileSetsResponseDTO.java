package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.List;

public class GetTileSetsResponseDTO extends ResponseDTO {

    private final List<Long> tileSets;

    public GetTileSetsResponseDTO(boolean succes, List<Long> tileSets, String errorMessage) {
        super(succes, errorMessage);
        this.tileSets = tileSets;
    }

    public List<Long> getTileSets() {
        return tileSets;
    }

    @Override
    public String toString() {
        return "GetTileSetsResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", tileSets=" + tileSets +
                '}';
    }
}
