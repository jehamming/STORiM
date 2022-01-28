package com.hamming.storim.server.game;

import com.hamming.storim.common.dto.VerbDetailsDTO;
import com.hamming.storim.common.dto.protocol.serverpush.MessageInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserLeftRoomDTO;
import com.hamming.storim.common.util.StringUtils;
import com.hamming.storim.server.STORIMMicroServer;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.factories.AvatarFactory;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.factories.ThingFactory;
import com.hamming.storim.server.common.factories.TileFactory;
import com.hamming.storim.server.common.model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController extends ServerWorker {
    private boolean running = true;
    private GameState gameState;
    private List<GameStateListener> gameStateListeners;

    public GameController() {
        gameState = new GameState();
        gameStateListeners = new ArrayList<>();
    }

    public void addOnlineUser(ClientConnection source, User u) {
        gameState.getOnlineUsers().add(u);
        fireGameStateEvent(source, GameStateEvent.Type.USERCONNECTED, u, null);
    }

    public void userDisconnected(ClientConnection source, User u) {
        boolean contained = gameState.getOnlineUsers().remove(u);
        if (contained) {
            fireGameStateEvent(source, GameStateEvent.Type.USERDISCONNECTED, u, null);
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    private void fireGameStateEvent(ClientConnection source, GameStateEvent.Type type, BasicObject object, Object extraData) {
        for (GameStateListener l : gameStateListeners) {
            if ( ! l.equals(source) ) { // No event to the source of this fireGamestateEven call
                l.newGameState(new GameStateEvent(type, object, extraData));
            }
        }
    }

    public void addGamestateListener(GameStateListener l) {
        gameStateListeners.add(l);
    }

    public void removeGameStateListener(GameStateListener l) {
        if (gameStateListeners.contains(l)) {
            gameStateListeners.remove(l);
        }
    }

    public void userLocationUpdated(ClientConnection source, User u) {
        fireGameStateEvent(source, GameStateEvent.Type.USERLOCATION, u, null);
    }

    public MessageInRoomDTO executeVeb(ClientConnection source, User u, VerbDetailsDTO verb, String input) {
        //FIXME Execute Verb
        Map<String, String> replacements = new HashMap<>();
        Location location = gameState.getLocation(u.getId());
        replacements.put(GameConstants.CMD_REPLACE_CALLER, u.getName());
        replacements.put(GameConstants.CMD_REPLACE_LOCATION, location.getRoom().getName());
        replacements.put(GameConstants.CMD_REPLACE_MESSAGE, input);

        String toCaller = StringUtils.replace(verb.getToCaller(), replacements);
        String toLocation = StringUtils.replace(verb.getToLocation(), replacements);
        fireGameStateEvent(source, GameStateEvent.Type.MSGINROOM, u, toLocation);
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(u.getId(), MessageInRoomDTO.Type.USER, toCaller);
        return messageInRoomDTO;
    }

    public void verbDeleted(ClientConnection source, Verb verb) {
        fireGameStateEvent(source, GameStateEvent.Type.VERBDELETED, verb, null);
    }

    public void roomDeleted(ClientConnection source, Room room) {
        fireGameStateEvent(source, GameStateEvent.Type.ROOMDELETED, room, null);
    }

    public void updateRoom(ClientConnection source, Long roomId, String name, int width, int length, int rows, int cols, byte[] imageData) {
        Room room = RoomFactory.getInstance().updateRoom(roomId, name, width, length, rows, cols);
        Image image = ImageUtils.decode(imageData);
        Tile tile = TileFactory.getInstance(STORIMMicroServer.DATADIR).createTile(room.getOwnerId(), image);
        room.setTileId(tile.getId());
        fireGameStateEvent(source,GameStateEvent.Type.ROOMUPDATED, room, null);
    }

    public void updateRoom(ClientConnection source, Long roomId, String name, int width, int length, int rows, int cols, Long tileId) {
        Room room = RoomFactory.getInstance().updateRoom(roomId, name, width, length, rows, cols);
        Tile tile = TileFactory.getInstance(STORIMMicroServer.DATADIR).findTileById(tileId);
        room.setTileId(tile.getId());
        fireGameStateEvent(source, GameStateEvent.Type.ROOMUPDATED, room, null);
    }

    public void addRoom(ClientConnection source, Long creatorId, String name, byte[] imageData) {
        Image image = ImageUtils.decode(imageData);
        Tile tile = TileFactory.getInstance(STORIMMicroServer.DATADIR).createTile(creatorId, image);
        tile.setCreatorId(creatorId);
        tile.setOwnerId(creatorId);
        addRoom(source, creatorId, name, tile);
    }

    public void addRoom(ClientConnection source,Long creatorId, String name, Long tileID) {
        // Create Tile
        Tile tile = TileFactory.getInstance(STORIMMicroServer.DATADIR).findTileById(tileID);
        addRoom(source, creatorId, name, tile);
    }

    private void addRoom(ClientConnection source, Long creatorId, String name, Tile tile) {
        Room room = RoomFactory.getInstance().createRoom(creatorId, name);
        room.setOwnerId(creatorId);
        room.setTileId(tile.getId());
        fireGameStateEvent(source, GameStateEvent.Type.ROOMADDED, room, null);
    }

    public void addRoom(ClientConnection source, Long creatorId, String name) {
        Room room = RoomFactory.getInstance().createRoom(creatorId, name);
        room.setOwnerId(creatorId);
        fireGameStateEvent(source, GameStateEvent.Type.ROOMADDED, room, null);
    }

    public void addAvatar(ClientConnection source, Long creatorId, String name, Image image) {
        Avatar avatar = AvatarFactory.getInstance(STORIMMicroServer.DATADIR).createAvatar(creatorId, name, image);
        fireGameStateEvent(source, GameStateEvent.Type.AVATARADDED, avatar, null);
    }

    public void updateAvatar(ClientConnection source, Long avatarId, String name, Image image) {
        Avatar avatar = AvatarFactory.getInstance(STORIMMicroServer.DATADIR).findAvatarById(avatarId);
        avatar.setName(name);
        avatar.setImage(image);
        fireGameStateEvent(source, GameStateEvent.Type.AVATARUPDATED, avatar, null);
    }

    public void deleteAvatar(ClientConnection source, Avatar avatar) {
        AvatarFactory.getInstance(STORIMMicroServer.DATADIR).deleteAvatar(avatar);
        for (User u : gameState.getOnlineUsers()) {
            if (u.getCurrentAvatar() != null && u.getCurrentAvatar().getId().equals(avatar.getId())) {
                u.setCurrentAvatar(null);
                fireGameStateEvent(source, GameStateEvent.Type.USERUPDATED, u, null);
            }
        }
        fireGameStateEvent(source, GameStateEvent.Type.AVATARDELETED, avatar, null);
    }

    public void updateUser(ClientConnection source,Long id, String name, String email, Long avatarID) {
        User user = gameState.findUserById(id);
        if (user != null) {
            if (name != null) user.setName(name);
            if (email != null) user.setEmail(email);
            if (avatarID != null) {
                Avatar avatar = AvatarFactory.getInstance(STORIMMicroServer.DATADIR).findAvatarById(avatarID);
                user.setCurrentAvatar(avatar);
            }
            fireGameStateEvent(source, GameStateEvent.Type.USERUPDATED, user, null);
        }
    }


    public void addThing(ClientConnection source, Long creatorId, String name, String description, float scale, int rotation, Image image) {
        Thing thing = ThingFactory.getInstance(STORIMMicroServer.DATADIR).createThing(creatorId, name, description, scale, rotation, image);
        fireGameStateEvent(source, GameStateEvent.Type.THINGADDED, thing, null);
    }

    public void deleteThing(ClientConnection source,Thing thing) {
        ThingFactory.getInstance(STORIMMicroServer.DATADIR).deleteThing(thing);
        fireGameStateEvent(source, GameStateEvent.Type.THINGDELETED, thing, null);
    }

    public void updateThing(ClientConnection source, Long id, String name, String description, float scale, int rotation, Image image) {
        Thing thing = ThingFactory.getInstance(STORIMMicroServer.DATADIR).findThingById(id);
        if (thing != null) {
            thing.setName(name);
            thing.setDescription(description);
            thing.setScale(scale);
            thing.setRotation(rotation);
            thing.setImage(image);
            fireGameStateEvent(source, GameStateEvent.Type.THINGUPDATED, thing, null);
        }

    }

    public void placeThingInRoom(ClientConnection source,User user, Thing thing, Room room) {
        Location location = new Location(room, room.getSpawnPointX(), room.getSpawnPointY());
        thing.setLocation(location);
        fireGameStateEvent(source, GameStateEvent.Type.THINGPLACED, thing, user);
    }

    public void updateThingLocation(ClientConnection source,Thing thing, int x, int y) {
        if (thing != null) {
            thing.getLocation().setX(x);
            thing.getLocation().setY(y);
            fireGameStateEvent(source, GameStateEvent.Type.THINGUPDATED, thing, null);
        }
    }

    public void userLeftRoom(ClientConnection source, User user, Long fromRoomId, Long newRoomId, boolean teleported) {
        Room newRoom = RoomFactory.getInstance().findRoomByID(newRoomId);
        UserLeftRoomDTO userLeftRoomDTO = new UserLeftRoomDTO(user.getId(), user.getName(), fromRoomId, newRoomId, newRoom.getName(), teleported);
        fireGameStateEvent(source, GameStateEvent.Type.USERLEFTROOM, user, userLeftRoomDTO);
    }
}