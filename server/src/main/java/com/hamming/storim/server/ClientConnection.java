package com.hamming.storim.server;


import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.UserInRoomDTO;
import com.hamming.storim.common.dto.protocol.UserLocationUpdateDTO;
import com.hamming.storim.common.dto.protocol.UserTeleportedDTO;
import com.hamming.storim.common.dto.protocol.avatar.AvatarAddedDTO;
import com.hamming.storim.common.dto.protocol.avatar.AvatarDeletedDTO;
import com.hamming.storim.common.dto.protocol.avatar.AvatarUpdatedDTO;
import com.hamming.storim.common.dto.protocol.avatar.GetAvatarResultDTO;
import com.hamming.storim.common.dto.protocol.room.GetRoomResultDTO;
import com.hamming.storim.common.dto.protocol.room.GetTileResultDTO;
import com.hamming.storim.common.dto.protocol.room.RoomAddedDTO;
import com.hamming.storim.common.dto.protocol.room.RoomUpdatedDTO;
import com.hamming.storim.common.dto.protocol.thing.*;
import com.hamming.storim.common.dto.protocol.user.GetUserResultDTO;
import com.hamming.storim.common.dto.protocol.user.UserUpdatedDTO;
import com.hamming.storim.common.dto.protocol.verb.ExecVerbResultDTO;
import com.hamming.storim.common.dto.protocol.verb.GetVerbResultDTO;
import com.hamming.storim.server.factories.AvatarFactory;
import com.hamming.storim.server.factories.DTOFactory;
import com.hamming.storim.server.factories.ThingFactory;
import com.hamming.storim.server.factories.TileFactory;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.GameProtocolHandler;
import com.hamming.storim.server.game.GameStateEvent;
import com.hamming.storim.server.game.GameStateListener;
import com.hamming.storim.server.game.action.Action;
import com.hamming.storim.server.game.action.UserConnectedAction;
import com.hamming.storim.server.game.action.UserDisconnectedAction;
import com.hamming.storim.server.game.action.UserOnlineAction;
import com.hamming.storim.server.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection implements Runnable, GameStateListener {

    private User currentUser;
    private Socket socket;
    private ObjectInputStream in;
    private boolean running = true;
    private GameController gameController;
    private GameProtocolHandler gameProtocolHandler;
    private ClientSender clientSender;
    private String id;

    public ClientConnection(String id, Socket s, ObjectInputStream in, ObjectOutputStream out, GameController controller) {
        this.socket = s;
        this.in = in;
        this.id = id;
        this.gameController = controller;
        this.gameProtocolHandler = new GameProtocolHandler(controller, this);
        clientSender = new ClientSender(out);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Object read = in.readObject();
                ProtocolDTO dto = (ProtocolDTO) read;
                System.out.println("RECEIVED:" + dto);
                handleInput(dto);
            } catch (IOException e) {
                //System.out.println(this.getClass().getName() + ":" + "IO Error:" + e.getMessage());
                // e.printStackTrace();
                running = false;
            } catch (ClassNotFoundException e) {
                System.out.println(this.getClass().getName() + ":" + "Error:" + e.getMessage());
                e.printStackTrace();
            }
        }
        gameController.removeListener(this);
        clientSender.stopSending();
        if (currentUser != null) {
            //gameController.userDisconnected(currentUser);
            UserDisconnectedAction action = new UserDisconnectedAction(gameController, this, currentUser);
            gameController.addAction(action);
        }
        try {
            socket.close();
        } catch (IOException e) {
        }
        System.out.println(this.getClass().getName() + ":" + "Client Socket closed");
    }

    private void handleInput(ProtocolDTO dto) {
        Action action = gameProtocolHandler.getAction(dto);
        if (action != null) {
            action.setDTO(dto);
            if (action != null) {
                gameController.addAction(action);
            }
        } else {
            System.out.println("NOT HANDLED:" + dto.getClass().getSimpleName());
        }
    }

    @Override
    public void newGameState(GameStateEvent event) {
        switch (event.getType()) {
            case USERCONNECTED:
                handleUserConnected((User) event.getObject());
                break;
            case USERDISCONNECTED:
                handleUserDisconnected((User) event.getObject());
                break;
            case USERUPDATED:
                handleUserUpdated((User) event.getObject());
                break;
            case USERLOCATION:
                handleUserLocation((User) event.getObject());
                break;
            case USERTELEPORTED:
                handleTeleported((User) event.getObject(), (Long) event.getExtraData());
                break;
            case VERBEXECUTED:
                handleVerbExecuted((VerbOutput) event.getObject());
                break;
            case VERBDELETED:
                handleVerbDeleted((Verb) event.getObject());
                break;
            case ROOMADDED:
                handleRoomAdded((Room) event.getObject());
                break;
            case ROOMDELETED:
                handleRoomDeleted((Room) event.getObject());
                break;
            case ROOMUPDATED:
                sendRoomUpdate((Room) event.getObject());
                break;
            case AVATARADDED:
                avatarAdded((Avatar) event.getObject());
                break;
            case AVATARDELETED:
                avatarDeleted((Avatar) event.getObject());
                break;
            case AVATARUPDATED:
                avatarUpdated((Avatar) event.getObject());
                break;
            case THINGADDED:
                thingAdded((Thing) event.getObject());
                break;
            case THINGDELETED:
                thingDeleted((Thing) event.getObject());
                break;
            case THINGUPDATED:
                thingUpdated((Thing) event.getObject());
                break;
            case THINGPLACED:
                thingPlaced((User) event.getExtraData(), (Thing) event.getObject());
                break;
        }
    }

    private void thingPlaced(User user, Thing thing) {
        ThingDto thingDto = DTOFactory.getInstance().getThingDTO(thing);
        ThingPlacedDTO dto = new ThingPlacedDTO(user.getId(),thingDto);
        send(dto);
    }


    private void thingDeleted(Thing thing) {
        ThingDto thingDto = DTOFactory.getInstance().getThingDTO(thing);
        ThingDeletedDTO dto = new ThingDeletedDTO(thingDto);
        send(dto);
    }


    private void thingUpdated(Thing thing) {
        ThingDto thingDto = DTOFactory.getInstance().getThingDTO(thing);
        ThingUpdatedDTO dto = new ThingUpdatedDTO(thingDto);
        send(dto);
    }


    private void avatarUpdated(Avatar avatar) {
        AvatarDto avatarDto = DTOFactory.getInstance().getAvatarDTO(avatar);
        AvatarUpdatedDTO avatarUpdatedDTO = new AvatarUpdatedDTO(avatarDto);
        send(avatarUpdatedDTO);
    }

    private void avatarDeleted(Avatar avatar) {
        AvatarDeletedDTO avatarDeletedDTO = new AvatarDeletedDTO(avatar.getId());
        send(avatarDeletedDTO);
    }

    private void handleUserUpdated(User user) {
        UserDto userDto = DTOFactory.getInstance().getUserDTO(user);
        if (!user.getId().equals(currentUser.getId()) ){
            sendAvatar(user);
        }
        UserUpdatedDTO userUpdatedDTO = new UserUpdatedDTO(userDto);
        send(userUpdatedDTO);
    }

    public void send(ProtocolDTO dto) {
        clientSender.enQueue(dto);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        if (currentUser != null) {
            gameController.userConnected(currentUser);
        }
    }

    public void sendFullGameState() {
        if (isLoggedIn()) {
            // Send Commnds
            sendUserCommands();
            // Send Tiles
            sendTiles();
            // Send Avatars
            sendAvatars();
            // Rooms
            sendRooms();
            // Send Things
            sendThings();
            // Send Things In Room
            sendThingsInRoom();
            // Logged in Users;
            for (User u : gameController.getGameState().getOnlineUsers()) {
                if (!u.getId().equals(currentUser.getId())) {
                    sendUserDetails(u);
                    handleUserOnline(u);
                }
            }
        }
    }

    private void sendThingsInRoom() {
        for (Thing thing : ThingFactory.getInstance().getAllThingsInRoom(currentUser.getLocation().getRoom().getId()) ) {
            ThingDto thingDto = DTOFactory.getInstance().getThingDTO(thing);
            GetThingResultDTO getThingResultDTO = new GetThingResultDTO(true, null, thingDto);
            send(getThingResultDTO);
        }
    }

    private void sendThings() {
        for (Thing thing : ThingFactory.getInstance().getThings(currentUser)) {
            sendThing(thing);
        }
    }

    public void sendThing(Thing thing) {
        ThingDto thingDto = DTOFactory.getInstance().getThingDTO(thing);
        GetThingResultDTO getThingResultDTO = new GetThingResultDTO(true, null, thingDto);
        send(getThingResultDTO);
    }

    private void sendAvatars() {
        for (Avatar avatar : AvatarFactory.getInstance().getAvatars(currentUser)) {
            sendAvatar(avatar);
        }
    }

    public void sendAvatar(Avatar avatar) {
        AvatarDto avatarDto = DTOFactory.getInstance().getAvatarDTO(avatar);
        GetAvatarResultDTO getAvatarResultDTO = DTOFactory.getInstance().getGetAvatarResultDTO(true, null, currentUser.getId(), avatarDto);
        send(getAvatarResultDTO);
    }

    public void sendAvatar(User user) {
        if (user.getCurrentAvatar() != null) {
            sendAvatar(user.getCurrentAvatar());
        }
    }


    private void sendTiles() {
        for (Tile tile : TileFactory.getInstance().geTiles(currentUser)) {
            sendTile(tile);
        }
    }

    private void sendRooms() {
        for (Room room : Database.getInstance().getAll(Room.class, currentUser.getId())) {
            sendRoom(room);
        }
    }

    private void sendUserCommands() {
        for (Verb verb : Database.getInstance().getAll(Verb.class, currentUser.getId())) {
            VerbDto verbDto = DTOFactory.getInstance().getVerbDto(verb);
            GetVerbResultDTO getCommandResultDTO = DTOFactory.getInstance().getVerbResultDto(true, null, verbDto);
            send(getCommandResultDTO);
        }
    }

    private boolean isOnline(User user) {
        return gameController.getGameState().getOnlineUsers().contains(user);
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    private void avatarAdded(Avatar avatar) {
        AvatarDto avatarDto = DTOFactory.getInstance().getAvatarDTO(avatar);
        if (avatar.getOwner().getId().equals(currentUser.getId())) {
            AvatarAddedDTO avatarAddedDTO = DTOFactory.getInstance().getAvatarAddedDTO(avatarDto);
            send(avatarAddedDTO);
        }
    }

    private void thingAdded(Thing thing) {
        ThingDto thingDto = DTOFactory.getInstance().getThingDTO(thing);
        if (thing.getOwner().getId().equals(currentUser.getId())) {
            ThingAddedDTO thingAddedDTO = new ThingAddedDTO(thingDto);
            send(thingAddedDTO);
        }
    }


    private void handleRoomAdded(Room room) {
        RoomDto roomDTO = DTOFactory.getInstance().getRoomDto(room);
        if (room.getTile() != null) {
            sendTile(room.getTile());
        }
        RoomAddedDTO roomAddedDTO = DTOFactory.getInstance().getRoomAddedDTO(roomDTO);
        send(roomAddedDTO);
    }


    private void sendRoomUpdate(Room room) {
        RoomDto roomDTO = DTOFactory.getInstance().getRoomDto(room);
        if (room.getTile() != null) {
            sendTile(room.getTile());
        }
        RoomUpdatedDTO roomUpdatedDTO = DTOFactory.getInstance().getRoomUpdatedDTO(roomDTO);
        send(roomUpdatedDTO);
    }

    private void sendTile(Tile tile) {
        TileDto tileDto = DTOFactory.getInstance().getTileDTO(tile);
        GetTileResultDTO getTileResultDTO = DTOFactory.getInstance().getGetTileResultDTO(true, null, tileDto);
        send(getTileResultDTO);
    }

    private void handleRoomDeleted(Room room) {
        send(DTOFactory.getInstance().getRoomDeletedDTO(room));
    }

    private void handleVerbDeleted(Verb verb) {
        send(DTOFactory.getInstance().getVerbDeletedDTO(verb));
    }

    private void handleVerbExecuted(VerbOutput cmdResult) {
        if (isInCurrentRoom(cmdResult.getCaller())) {
            String output = "";
            if (cmdResult.getCaller().getId().equals(currentUser.getId())) {
                output = cmdResult.getToCaller();
            } else {
                output = cmdResult.getToLocation();
            }
            ExecVerbResultDTO execVerbResultDTO = DTOFactory.getInstance().getExecVerbResultDto(cmdResult.getId(), output);
            send(execVerbResultDTO);
        }
    }

    private boolean isInCurrentRoom(User caller) {
        return currentUser.getLocation().getRoom().getId().equals(caller.getLocation().getRoom().getId());
    }


    private void handleTeleported(User user, Long oldRoomId) {
        if (user.equals(currentUser)) {
            sendUsersInRoom(user.getLocation().getRoom());
        } else {
            sendRoom(user.getLocation().getRoom());
            if (user.getLocation().getRoom().getTile() != null) {
                sendTile(user.getLocation().getRoom().getTile());
            }
            LocationDto location = DTOFactory.getInstance().getLocationDTO(user.getLocation());
            UserTeleportedDTO userTeleportedDTO = DTOFactory.getInstance().getUserTeleportedDTO(user, oldRoomId, location);
            send(userTeleportedDTO);
        }
    }

    public void sendUsersInRoom(Room room) {
        gameController.getGameState().getOnlineUsers().forEach(user -> {
            if (!user.equals(currentUser) && room.getId().equals(user.getLocation().getRoom().getId())) {
                LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(user.getLocation());
                sendAvatar(user);
                UserInRoomDTO dto = DTOFactory.getInstance().getUserInRoomDTO(user, room, locationDto);
                send(dto);
            }
        });
    }

    public void handleUserLocation(User user) {
        UserLocationUpdateDTO userLocationUpdateDTO = DTOFactory.getInstance().getUserLocationUpdateDTO(user);
        send(userLocationUpdateDTO);
    }

    public void sendRoom(Room room) {
        if (room.getTile() != null) {
            sendTile(room.getTile());
        }
        RoomDto roomDto = DTOFactory.getInstance().getRoomDto(room);
        GetRoomResultDTO getRoomResultDTO = DTOFactory.getInstance().getRoomResultDTO(true, null, roomDto);
        send(getRoomResultDTO);
    }

    private void sendUserDetails(User user) {
        UserDto userDto = DTOFactory.getInstance().getUserDTO(user);
        Room room = user.getLocation().getRoom();
        sendRoom(room);
        sendAvatar(user);
        GetUserResultDTO getUserResultDTO = DTOFactory.getInstance().getGetUserResultDTO(true, null, userDto);
        send(getUserResultDTO);
    }

    private void handleUserConnected(User u) {
        if (currentUser != null && !currentUser.equals(u)) {
            sendAvatar(u);
            UserConnectedAction action = new UserConnectedAction(gameController, this, u);
            gameController.addAction(action);
        }
    }

    private void handleUserOnline(User u) {
        if (currentUser != null && !currentUser.equals(u)) {
            UserOnlineAction action = new UserOnlineAction(gameController, this, u);
            gameController.addAction(action);
        }
    }

    private void handleUserDisconnected(User u) {
        if (currentUser != null && !currentUser.equals(u)) {
            UserDisconnectedAction action = new UserDisconnectedAction(gameController, this, u);
            gameController.addAction(action);
        }
    }

    public GameProtocolHandler getProtocolHandler() {
        return gameProtocolHandler;
    }
}
