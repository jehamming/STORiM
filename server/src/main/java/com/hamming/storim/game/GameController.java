package com.hamming.storim.game;

import com.hamming.storim.factories.RoomFactory;
import com.hamming.storim.factories.TileFactory;
import com.hamming.storim.factories.VerbResultFactory;
import com.hamming.storim.game.action.Action;
import com.hamming.storim.model.*;
import com.hamming.storim.util.ImageUtils;
import com.hamming.storim.util.StringUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GameController implements Runnable {
    private Deque<Action> actionQueue;
    private boolean running = true;
    private GameState gameState;
    private List<GameStateListener> listeners;

    public GameController() {
        gameState = new GameState();
        listeners = new ArrayList<GameStateListener>();
    }

    @Override
    public void run() {
        actionQueue = new ArrayDeque<Action>();
        while (running) {
            if (actionQueue.isEmpty()) {
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    System.out.println(this.getClass().getName() + ":" + "Exception : method wait was interrupted!");
                }
            }
            while (!actionQueue.isEmpty()) {
                Action cmd = actionQueue.removeFirst();
                cmd.execute();
            }
        }
    }

    public void userConnected(User u) {
        gameState.getOnlineUsers().add(u);
        fireGameStateEvent(GameStateEvent.Type.USERCONNECTED, u, null);
    }

    public void userDisconnected(User u) {
        boolean contained = gameState.getOnlineUsers().remove(u);
        if (contained) {
            fireGameStateEvent(GameStateEvent.Type.USERDISCONNECTED, u, null);
        }
    }


    public void addAction(Action cmd) {
        actionQueue.addLast(cmd);
        synchronized (this) {
            this.notify();
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    private void fireGameStateEvent(GameStateEvent.Type type, BasicObject object, Object extraData) {
        for (GameStateListener l : listeners) {
            if (l == null) {
                // Should not happen, stale client connection..
                removeListener(l);
            } else {
                l.newGameState(new GameStateEvent(type, object, extraData));
            }
        }
    }

    public void addListener(GameStateListener l) {
        listeners.add(l);
    }

    public void removeListener(GameStateListener l) {
        if (listeners.contains(l)) {
            listeners.remove(l);
        }
    }

    public void setLocation(User u, Location location) {
        fireGameStateEvent(GameStateEvent.Type.USERLOCATION, u, null);
    }

    public void userTeleported(User u, Long fromRoomId, Location loc) {
        fireGameStateEvent(GameStateEvent.Type.USERTELEPORTED, u, fromRoomId);
    }

    public void executeVeb(User u, Verb vebr, String input) {
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put(GameConstants.CMD_REPLACE_CALLER, u.getName());
        replacements.put(GameConstants.CMD_REPLACE_LOCATION, u.getLocation().getRoom().getName());
        replacements.put(GameConstants.CMD_REPLACE_MESSAGE, input);

        String toCaller = StringUtils.replace(vebr.getToCaller(), replacements);
        String toLocation = StringUtils.replace(vebr.getToLocation(), replacements);

        VerbOutput cmdResult = VerbResultFactory.getInstance().createCommandResult(vebr,u);
        cmdResult.setToCaller(toCaller);
        cmdResult.setToLocation(toLocation);
        fireGameStateEvent(GameStateEvent.Type.VERBEXECUTED, cmdResult, null);
    }

    public void verbDeleted(Verb verb) {
        fireGameStateEvent(GameStateEvent.Type.VERBDELETED, verb, null);
    }

    public void roomDeleted(Room room) {
        fireGameStateEvent(GameStateEvent.Type.ROOMDELETED, room, null);
    }

    public void updateRoom(Long roomId, String name, Integer size, byte[] imageData) {
        Room room = RoomFactory.getInstance().updateRoom(roomId, name, size);
        Image image = ImageUtils.decode(imageData);
        Tile tile = TileFactory.getInstance().createTile(room.getOwner(), image);
        room.setTile(tile);
        fireGameStateEvent(GameStateEvent.Type.ROOMUPDATED, room, null);
    }

    public void updateRoom(Long roomId, String name, Integer size, Long tileId) {
        Room room = RoomFactory.getInstance().updateRoom(roomId, name, size);
        Tile tile = TileFactory.getInstance().findTileById(tileId);
        room.setTile(tile);
        fireGameStateEvent(GameStateEvent.Type.ROOMUPDATED, room, null);
    }

    public void addRoom(User creator, String name, Integer size, byte[] imageData) {
        Image image = ImageUtils.decode(imageData);
        Tile tile = TileFactory.getInstance().createTile(creator, image);
        tile.setCreator(creator);
        tile.setOwner(creator);
        addRoom(creator, name, size, tile);
    }

    public void addRoom(User creator, String name, Integer size, Long tileID) {
        // Create Tile
        Tile tile = TileFactory.getInstance().findTileById(tileID);
        addRoom(creator, name, size, tile);
    }

    private void addRoom(User creator, String name, Integer size, Tile tile) {
        Room room = RoomFactory.getInstance().createRoom(creator, name, size);
        room.setOwner(creator);
        room.setTile(tile);
        creator.addRoom(room);
        fireGameStateEvent(GameStateEvent.Type.ROOMADDED, room, null);
    }

    public void addRoom(User creator, String name, Integer size) {
        Room room = RoomFactory.getInstance().createRoom(creator, name, size);
        room.setOwner(creator);
        creator.addRoom(room);
        fireGameStateEvent(GameStateEvent.Type.ROOMADDED, room, null);
    }

}