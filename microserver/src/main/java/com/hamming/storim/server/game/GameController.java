package com.hamming.storim.server.game;

import com.hamming.storim.common.util.StringUtils;
import com.hamming.storim.server.STORIMMicroServer;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.UserCache;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.factories.*;
import com.hamming.storim.server.common.model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController extends ServerWorker {
    private boolean running = true;
    private GameState gameState;
    private List<GameStateListener> listeners;

    public GameController() {
        gameState = new GameState();
        listeners = new ArrayList<>();
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


    public GameState getGameState() {
        return gameState;
    }

    public void fireGameStateEvent(GameStateEvent.Type type, BasicObject object, Object extraData) {
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

    public void userLocationUpdated(User u) {
        fireGameStateEvent(GameStateEvent.Type.USERLOCATION, u, null);
    }

    public void executeVeb(User u, Verb verb, String input) {
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put(GameConstants.CMD_REPLACE_CALLER, u.getName());
        replacements.put(GameConstants.CMD_REPLACE_LOCATION, u.getLocation().getRoom().getName());
        replacements.put(GameConstants.CMD_REPLACE_MESSAGE, input);

        String toCaller = StringUtils.replace(verb.getToCaller(), replacements);
        String toLocation = StringUtils.replace(verb.getToLocation(), replacements);

        VerbOutput cmdResult = VerbResultFactory.getInstance().createCommandResult(verb,u.getId());
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

    public void updateRoom(Long roomId, String name, int width, int length, int rows, int cols, byte[] imageData) {
        Room room = RoomFactory.getInstance().updateRoom(roomId, name, width, length, rows, cols);
        Image image = ImageUtils.decode(imageData);
        Tile tile = TileFactory.getInstance(STORIMMicroServer.DATADIR).createTile(room.getOwnerId(), image);
        room.setTileId(tile.getId());
        fireGameStateEvent(GameStateEvent.Type.ROOMUPDATED, room, null);
    }

    public void updateRoom(Long roomId, String name, int width, int length, int rows, int cols, Long tileId) {
        Room room = RoomFactory.getInstance().updateRoom(roomId, name, width, length, rows, cols);
        Tile tile = TileFactory.getInstance(STORIMMicroServer.DATADIR).findTileById(tileId);
        room.setTileId(tile.getId());
        fireGameStateEvent(GameStateEvent.Type.ROOMUPDATED, room, null);
    }

    public void addRoom(Long creatorId, String name, byte[] imageData) {
        Image image = ImageUtils.decode(imageData);
        Tile tile = TileFactory.getInstance(STORIMMicroServer.DATADIR).createTile(creatorId, image);
        tile.setCreatorId(creatorId);
        tile.setOwnerId(creatorId);
        addRoom(creatorId, name, tile);
    }

    public void addRoom(Long creatorId, String name, Long tileID) {
        // Create Tile
        Tile tile = TileFactory.getInstance(STORIMMicroServer.DATADIR).findTileById(tileID);
        addRoom(creatorId, name, tile);
    }

    private void addRoom(Long creatorId, String name, Tile tile) {
        Room room = RoomFactory.getInstance().createRoom(creatorId, name);
        room.setOwnerId(creatorId);
        room.setTileId(tile.getId());
        fireGameStateEvent(GameStateEvent.Type.ROOMADDED, room, null);
    }

    public void addRoom(Long creatorId, String name) {
        Room room = RoomFactory.getInstance().createRoom(creatorId, name);
        room.setOwnerId(creatorId);
        fireGameStateEvent(GameStateEvent.Type.ROOMADDED, room, null);
    }

    public void addAvatar(Long creatorId, String name, Image image) {
        Avatar avatar = AvatarFactory.getInstance(STORIMMicroServer.DATADIR).createAvatar(creatorId, name, image);
        fireGameStateEvent(GameStateEvent.Type.AVATARADDED, avatar, null);
    }

    public void updateAvatar(Long avatarId, String name, Image image) {
        Avatar avatar = AvatarFactory.getInstance(STORIMMicroServer.DATADIR).findAvatarById(avatarId);
        avatar.setName(name);
        avatar.setImage(image);
        fireGameStateEvent(GameStateEvent.Type.AVATARUPDATED, avatar, null);
    }

    public void deleteAvatar(Avatar avatar) {
        AvatarFactory.getInstance(STORIMMicroServer.DATADIR).deleteAvatar(avatar);
        for (User u : gameState.getOnlineUsers() ) {
            if ( u.getCurrentAvatar() != null && u.getCurrentAvatar().getId().equals(avatar.getId())) {
                u.setCurrentAvatar(null);
                fireGameStateEvent(GameStateEvent.Type.USERUPDATED, u, null);
            }
        }
        fireGameStateEvent(GameStateEvent.Type.AVATARDELETED, avatar, null);
    }

    public void updateUser(Long id, String name, String email, Long avatarID) {
        User user = UserCache.getInstance().findUserById(id);
        if ( user != null ) {
            if (name != null) user.setName(name);
            if (email != null) user.setEmail(email);
            if (avatarID != null) {
                Avatar avatar = AvatarFactory.getInstance(STORIMMicroServer.DATADIR).findAvatarById(avatarID);
                user.setCurrentAvatar(avatar);
            }
            fireGameStateEvent(GameStateEvent.Type.USERUPDATED, user, null);
        }
    }


    public void addThing(Long creatorId, String name, String description, float scale, int rotation, Image image) {
        Thing thing = ThingFactory.getInstance(STORIMMicroServer.DATADIR).createThing(creatorId, name, description, scale, rotation, image);
        fireGameStateEvent(GameStateEvent.Type.THINGADDED, thing, null);
    }

    public void deleteThing(Thing thing) {
        ThingFactory.getInstance(STORIMMicroServer.DATADIR).deleteThing(thing);
        fireGameStateEvent(GameStateEvent.Type.THINGDELETED, thing, null);
    }

    public void updateThing(Long id, String name, String description, float scale, int rotation, Image image) {
        Thing thing = ThingFactory.getInstance(STORIMMicroServer.DATADIR).findThingById(id);
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