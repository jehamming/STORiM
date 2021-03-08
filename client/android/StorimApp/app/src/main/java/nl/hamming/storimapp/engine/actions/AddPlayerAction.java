package nl.hamming.storimapp.engine.actions;

import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Player;


public class AddPlayerAction implements Action {

    private GameView viewer;
    private String userId, name;

    public AddPlayerAction(GameView viewer, String userId, String name) {
        this.viewer = viewer;
        this.userId = userId;
        this.name = name;
    }

    @Override
    public void execute() {
        Player player = new Player(userId);
        viewer.addPlayer(player);
    }

}
