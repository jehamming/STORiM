package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class TileAddedDTO extends ResponseDTO {


    private TileDto tile;

    public TileAddedDTO(TileDto tile) {
        super(true, null);
        this.tile = tile;
    }

    public TileDto getTile() {
        return tile;
    }

    @Override
    public String toString() {
        return "TileAddedDTO{" +
                "tile=" + tile +
                '}';
    }
}
