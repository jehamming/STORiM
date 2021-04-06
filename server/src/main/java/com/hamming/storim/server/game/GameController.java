package com.hamming.storim.server.game;

import com.hamming.storim.common.util.StringUtils;
import com.hamming.storim.server.ImageUtils;
import com.hamming.storim.server.factories.*;
import com.hamming.storim.server.game.action.Action;
import com.hamming.storim.server.model.*;

import java.awt.*;
import java.util.List;
import java.util.*;

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
        fireGameStateEvent(GameStateEvent.Type.ROOMADDED, room, null);
    }

    public void addRoom(User creator, String name, Integer size) {
        Room room = RoomFactory.getInstance().createRoom(creator, name, size);
        room.setOwner(creator);
        fireGameStateEvent(GameStateEvent.Type.ROOMADDED, room, null);
    }

    public void addAvatar(User creator, String name, Image image) {
        Avatar avatar = AvatarFactory.getInstance().createAvatar(creator, name, image);
        fireGameStateEvent(GameStateEvent.Type.AVATARADDED, avatar, null);
    }

    public void updateAvatar(Long avatarId, String name, Image image) {
        Avatar avatar = AvatarFactory.getInstance().findAvatarById(avatarId);
        avatar.setName(name);
        avatar.setImage(image);
        fireGameStateEvent(GameStateEvent.Type.AVATARUPDATED, avatar, null);
    }

    public void deleteAvatar(Avatar avatar) {
        AvatarFactory.getInstance().deleteAvatar(avatar);
        for (User u : gameState.getOnlineUsers() ) {
            if ( u.getCurrentAvatar() != null && u.getCurrentAvatar().getId().equals(avatar.getId())) {
                u.setCurrentAvatar(null);
                fireGameStateEvent(GameStateEvent.Type.USERUPDATED, u, null);
            }
        }
        fireGameStateEvent(GameStateEvent.Type.AVATARDELETED, avatar, null);
    }

    public void updateUser(Long id, String name, String email, Long avatarID) {
        User user = UserFactory.getInstance().findUserById(id);
        if ( user != null ) {
            if (name != null) user.setName(name);
            if (email != null) user.setEmail(email);
            if (avatarID != null) {
                Avatar avatar = AvatarFactory.getInstance().findAvatarById(avatarID);
                user.setCurrentAvatar(avatar);
            }
            fireGameStateEvent(GameStateEvent.Type.USERUPDATED, user, null);
        }
    }


    public void addThing(User creator, String name, String description, float scale, int rotation, Image image) {
        Thing thing = ThingFactory.getInstance().createThing(creator, name, description, scale, rotation, image);
        fireGameStateEvent(GameStateEvent.Type.THINGADDED, thing, null);
    }

    public void deleteThing(Thing thing) {
        ThingFactory.getInstance().deleteThing(thing);
        fireGameStateEvent(GameStateEvent.Type.THINGDELETED, thing, null);
    }

    public void updateThing(Long id, String name, String description, float scale, int rotation, Image image) {
        Thing thing = ThingFactory.getInstance().findThingById(id);
        if ( thing != null ) {
            thing.setName(name);
            thing.setDescription(description);
            thing.setScale(scale);
            thing.setRotation(rotation);
            thing.setImage(image);
            fireGameStateEvent(GameStateEvent.Type.THINGUPDATED, thing, null);
        }

    }

    public void placeThingInRoom(User user, Thing thing, Room room) {
        Location location = new Location(room, room.getSpawnPointX(), room.getSpawnPointY());
        thing.setLocation(location);
        fireGameStateEvent(GameStateEvent.Type.THINGPLACED, thing, user);
    }

    public void updateThingLocation(Thing thing, int x, int y) {
        if ( thing != null ) {
            thing.getLocation().setX(x);
            thing.getLocation().setY(y);
            fireGameStateEvent(GameStateEvent.Type.THINGUPDATED, thing, null);
        }
    }
}