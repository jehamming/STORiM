package nl.hamming.storimapp.engine.actions;

import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Player;

public class RemovePlayerAction implements Action {

    private GameView viewer;
    private String userId;

    public RemovePlayerAction(GameView viewer, String userId) {
        this.viewer = viewer;
        this.userId = userId;
    }

    @Override
    public void execute() {
        Player player = viewer.getPlayer(userId);
        viewer.removePlayer(player);
    }

}
