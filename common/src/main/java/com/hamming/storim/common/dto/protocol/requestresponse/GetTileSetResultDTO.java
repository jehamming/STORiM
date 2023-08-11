package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetTileSetResultDTO extends ResponseDTO {


    private TileSetDto tileSetDto;

    public GetTileSetResultDTO(TileSetDto tileSetDto) {
        this.tileSetDto = tileSetDto;
    }

    public TileSetDto getTileSetDto() {
        return tileSetDto;
    }

    @Override
    public String toString() {
        return "GetTileSetResultDTO{" +
                "tileSetDto=" + tileSetDto +
                '}';
    }
}
