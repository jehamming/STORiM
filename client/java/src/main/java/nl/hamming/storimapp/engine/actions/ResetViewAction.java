package nl.hamming.storimapp.engine.actions;

import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Player;

import java.util.ArrayList;


public class ResetViewAction implements Action {

    private GameView viewer;


    public ResetViewAction(GameView viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        viewer.setTileNoAction(null);
        viewer.setRoomNoAction(null);
        viewer.setPlayers(new ArrayList<Player>());
    }

    @Override
    public String toString() {
        return "ResetViewAction{" +
                '}';
    }
}
