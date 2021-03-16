package nl.hamming.storimapp.engine.actions;

import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Player;

import java.awt.*;


public class AddPlayerAction implements Action {

    private GameView viewer;
    private Long userId;
    private String name;
    private Image image;

    public AddPlayerAction(GameView viewer, Long userId, String name, Image image) {
        this.viewer = viewer;
        this.userId = userId;
        this.name = name;
        this.image = image;
    }

    @Override
    public void execute() {
        Player player = new Player(userId);
        player.setDisplayName(name);
        if (image != null ) {
            player.setAvatar(image);
        }
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
