package com.hamming.storim.client.engine.actions;

import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.client.view.GameView;


public class SetTileAction implements Action {

    private GameView viewer;
    private TileDto tile;


    public SetTileAction(GameView viewer, TileDto tile) {
        this.viewer = viewer;
        this.tile = tile;
    }

    @Override
    public void execute() {
        viewer.setTile(tile);
    }

    @Override
    public String toString() {
        return "SetTileAction{" +
                "tile=" + tile +
                '}';
    }
}
