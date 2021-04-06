package com.hamming.storim.client.engine.actions;

import com.hamming.storim.client.view.GameView;
import com.hamming.storim.client.view.Player;

public class DeleteAvatarAction implements Action {

    private GameView viewer;
    private Long userId;

    public DeleteAvatarAction(GameView viewer, Long userId ) {
        this.viewer = viewer;
        this.userId = userId;
    }

    @Override
    public void execute() {
        Player player = viewer.getPlayer(userId);
        player.setImage(null);
    }

    @Override
    public String toString() {
        return "DeleteAvatarAction{" +
                "userId=" + userId +
                '}';
    }
}
