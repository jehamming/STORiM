package com.hamming.storim.client.listitem;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.TileSetDto;

public class TileSetListItem {


    private TileSetDto tileSet;

    public TileSetListItem(TileSetDto tileSet) {
        this.tileSet = tileSet;
    }

    public TileSetDto getTileSet() {
        return tileSet;
    }

    public void setTileSet(TileSetDto tileSet) {
        this.tileSet = tileSet;
    }

    @Override
    public String toString() {
        return tileSet.getName();
    }


}
