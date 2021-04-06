package com.hamming.storim.client.engine.actions;

import com.hamming.storim.client.view.GameView;
import com.hamming.storim.client.view.Player;

public class RemovePlayerAction implements Action {

    private GameView viewer;
    private Long userId;

    public RemovePlayerAction(GameView viewer, Long userId) {
        this.viewer = viewer;
        this.userId = userId;
    }

    @Override
    public void execute() {
        Player player = viewer.getPlayer(userId);
        viewer.removePlayer(player);
    }

    @Override
    public String toString() {
        return "RemovePlayerAction{" +
                "userId=" + userId +
                '}';
    }
}
