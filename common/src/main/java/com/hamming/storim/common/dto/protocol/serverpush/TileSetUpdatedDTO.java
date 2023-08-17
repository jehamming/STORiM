package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class TileSetUpdatedDTO extends ResponseDTO {


    private TileSetDto tileSetDto;

    public TileSetUpdatedDTO(TileSetDto tileSetDto) {
        super(true, null);
        this.tileSetDto = tileSetDto;
    }


    public TileSetDto getTileSetDto() {
        return tileSetDto;
    }

    @Override
    public String toString() {
        return "TileSetUpdatedDTO{" +
                "tileSetDto=" + tileSetDto +
                '}';
    }
}
