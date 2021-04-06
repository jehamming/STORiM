package com.hamming.storim.client.engine.actions;

import com.hamming.storim.client.view.GameView;
import com.hamming.storim.client.view.Player;

public class SetUserLocationAction implements Action {

    private GameView gameView;
    private int x,y;
    private Long userId;

    public SetUserLocationAction(GameView gameView, Long userId, int x, int y) {
        this.gameView = gameView;
        this.userId = userId;
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute() {
        Player p = gameView.getPlayer(userId);
        //TODO Pitch an Yaw?
        p.setX(x);
        p.setY(y);
    }

    @Override
    public String toString() {
        return "SetUserLocationAction{" +
                "userId=" + userId +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
