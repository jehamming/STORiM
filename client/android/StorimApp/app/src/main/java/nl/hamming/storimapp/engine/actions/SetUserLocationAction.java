package nl.hamming.storimapp.engine.actions;

import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Player;

public class SetUserLocationAction implements Action {

    private GameView gameView;
    private float x,y;
    private String userId;

    public SetUserLocationAction(GameView gameView, String userId, float x, float y) {
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


}