package nl.hamming.storimapp.engine.actions;

import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.TileDto;
import nl.hamming.storimapp.view.GameView;


public class SetTileAction implements Action {

    private GameView viewer;
    private TileDto tile;


    public SetTileAction(GameView viewer, TileDto tile) {
        this.viewer = viewer;
        this.tile = tile;
    }

    @Override
    public void execute() {
        viewer.setTileNoAction(tile);
    }

    @Override
    public String toString() {
        return "SetTileAction{" +
                "tile=" + tile +
                '}';
    }
}
