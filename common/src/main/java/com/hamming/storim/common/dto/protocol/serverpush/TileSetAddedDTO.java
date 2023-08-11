package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class TileSetAddedDTO extends ResponseDTO {


    private TileSetDto tileSetDto;

    public TileSetAddedDTO(TileSetDto tileSetDto) {
        this.tileSetDto = tileSetDto;
    }


    public TileSetDto getTileSetDto() {
        return tileSetDto;
    }

    @Override
    public String toString() {
        return "TileSetAddedDTO{" +
                "tileSetDto=" + tileSetDto +
                '}';
    }
}
