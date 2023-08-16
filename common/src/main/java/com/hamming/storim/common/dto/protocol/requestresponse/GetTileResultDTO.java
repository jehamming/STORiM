package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetTileResultDTO extends ResponseDTO {


    private TileDto tile;

    public GetTileResultDTO(boolean success, TileDto tile, String errorMessage) {
        super(success, errorMessage);
        this.tile = tile;
    }

    public TileDto getTile() {
        return tile;
    }

    @Override
    public String toString() {
        return "GetTileResultDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", tile=" + tile +
                '}';
    }
}
