package nl.hamming.storimapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import nl.hamming.storimapp.controllers.MoveController;
import nl.hamming.storimapp.engine.actions.Action;
import nl.hamming.storimapp.engine.actions.AddPlayerAction;
import nl.hamming.storimapp.engine.actions.RemovePlayerAction;
import nl.hamming.storimapp.engine.actions.SetUserLocationAction;

public class GameView extends SurfaceView implements Runnable {


    //boolean variable to track if the game is playing or not
    volatile boolean playing;

    //the game thread
    private Thread gameThread = null;

    private List<Action> actions;
    private List<Player> players;
    private MoveController moveController;
    private boolean initialized;
    private String followedUserId;


    //Class constructor
    public GameView(Context context, AttributeSet attributeSet) {
        super(context);
        actions = Collections.synchronizedList(new LinkedList<Action>());
        players = new ArrayList<Player>();
        this.moveController = moveController;
    }

    public void setMoveController(MoveController moveController) {
        this.moveController = moveController;
    }

    @Override
    public void run() {
        while (playing) {

            handleActions();

            //to draw the frame
            draw();

            //to control
            control();
        }
    }


    private void handleActions() {
        synchronized (actions) {
            for (Action action : actions) {
                action.execute();
            }
            actions.removeAll(actions);
        }
    }

    public void resetView() {
        // TODO
    }


    public void addPlayer(String userId, String name) {
        Action action = new AddPlayerAction(this, userId, name);
        synchronized (actions) {
            actions.add(action);
        }
    }

    public Player getPlayer(String playerId) {
        Player player = null;
        for (Player p : players) {
            if (p.getUserId().equals(playerId)) {
                player = p;
                break;
            }
        }
        return  player;
    }

    public void removePlayer(String userId) {
        Action action = new RemovePlayerAction(this, userId);
        synchronized (actions) {
            actions.add(action);
        }
    }

    public void setLocation(String userId, float x, float y) {
        Action action = new SetUserLocationAction(this, userId, x, y);
        synchronized (actions) {
            actions.add(action);
        }
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void followPlayer(String userId) {
        followedUserId = userId;
    }

    private void draw() {

    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        //when the game is paused
        //setting the variable to false
        playing = false;
        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        //when the game is resumed
        //starting the thread again
        playing = true;
        gameThread = new Thread(this);





        gameThread.start();
    }
}


