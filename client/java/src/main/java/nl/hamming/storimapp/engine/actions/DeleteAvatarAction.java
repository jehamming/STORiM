package nl.hamming.storimapp.engine.actions;

import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Player;

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
        player.setAvatar(null);
    }

    @Override
    public String toString() {
        return "DeleteAvatarAction{" +
                "userId=" + userId +
                '}';
    }
}
