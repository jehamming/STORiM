package com.hamming.storim.client.listitem;

import com.hamming.storim.client.view.TileSet;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.TileSetDto;

public class TileSetListItem {


    private TileSet tileSet;

    public TileSetListItem(TileSet tileSet) {
        this.tileSet = tileSet;
    }

    public TileSet getTileSet() {
        return tileSet;
    }

    public void setTileSet(TileSet tileSet) {
        this.tileSet = tileSet;
    }

    @Override
    public String toString() {
        return tileSet.getName();
    }


}
