package nl.hamming.storimapp.engine.actions;

import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Player;


public class AddPlayerAction implements Action {

    private GameView viewer;
    private Long userId;
    private String name;

    public AddPlayerAction(GameView viewer, Long userId, String name) {
        this.viewer = viewer;
        this.userId = userId;
        this.name = name;
    }

    @Override
    public void execute() {
        Player player = new Player(userId);
        player.setDisplayName(name);
        viewer.addPlayer(player);
    }

    @Override
    public String toString() {
        return "AddPlayerAction{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                '}';
    }
}
