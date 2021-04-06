package com.hamming.storim.client.engine.actions;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.client.view.GameView;
import com.hamming.storim.client.view.Player;

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
            int roomSize = 10;
            int widthPerTile = viewer.getWidth() / roomSize;
            image = ImageUtils.resize(image, widthPerTile, widthPerTile);
            player.setImage(image);
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
