package com.hamming.storim.server;


import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.request.*;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.dto.protocol.serverpush.old.*;
import com.hamming.storim.common.net.ProtocolObjectSender;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.UpdateUserRoomDto;
import com.hamming.storim.server.common.dto.protocol.loginserver.VerifyUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.loginserver.VerifyUserResponseDTO;
import com.hamming.storim.server.common.factories.AvatarFactory;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.factories.ThingFactory;
import com.hamming.storim.server.common.factories.TileFactory;
import com.hamming.storim.server.common.model.*;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.GameStateEvent;
import com.hamming.storim.server.game.GameStateListener;
import com.hamming.storim.server.game.action.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class STORIMClientConnection extends ClientConnection implements GameStateListener {

    private User currentUser;
    private ProtocolObjectSender clientSender;
    private STORIMMicroServer server;
    private GameController gameController;

    public STORIMClientConnection(STORIMMicroServer server, ClientTypeDTO clientTypeDTO, Socket s, ObjectInputStream in, ObjectOutputStream out, GameController controller) {
        super(clientTypeDTO, s, in, out, controller);
        this.server = server;
        clientSender = new ProtocolObjectSender(out);
    }

    @Override
    public void connectionClosed() {
        gameController.removeGameStateListener(this);
        clientSender.stopSending();
        clientSender = null;
        UserDisconnectedAction action = new UserDisconnectedAction(gameController, this, currentUser);
        gameController.addAction(action);
        currentUser = null;
    }

    @Override
    public void addActions() {
        gameController = (GameController) getServerWorker();
        getProtocolHandler().addAction(TeleportRequestDTO.class, new TeleportAction(gameController, this));
        getProtocolHandler().addAction(GetRoomDTO.class, new GetRoomAction(gameController, this));
        getProtocolHandler().addAction(GetUserDTO.class, new GetUserAction(gameController, this));
        getProtocolHandler().addAction(GetVerbDTO.class, new GetVerbAction(gameController, this));
        getProtocolHandler().addAction(ExecVerbDTO.class, new ExecVerbAction(gameController, this));
        getProtocolHandler().addAction(AddVerbDto.class, new AddVerbAction(gameController, this));
        getProtocolHandler().addAction(UpdateVerbDto.class, new UpdateVerbAction(gameController, this));
        getProtocolHandler().addAction(DeleteVerbDTO.class, new DeleteVerbAction(gameController, this));
        getProtocolHandler().addAction(AddRoomDto.class, new AddRoomAction(gameController, this));
        getProtocolHandler().addAction(UpdateRoomDto.class, new UpdateRoomAction(gameController, this));
        getProtocolHandler().addAction(DeleteRoomDTO.class, new DeleteRoomAction(gameController, this));
        getProtocolHandler().addAction(AddAvatarDto.class, new AddAvatarAction(gameController, this));
        getProtocolHandler().addAction(UpdateUserDto.class, new UpdateUserAction(gameController, this));
        getProtocolHandler().addAction(DeleteAvatarDTO.class, new DeleteAvatarAction(gameController, this));
        getProtocolHandler().addAction(UpdateAvatarDto.class, new UpdateAvatarAction(gameController, this));
        getProtocolHandler().addAction(AddThingDto.class, new AddThingAction(gameController, this));
        getProtocolHandler().addAction(DeleteThingDTO.class, new DeleteThingAction(gameController, this));
        getProtocolHandler().addAction(UpdateThingDto.class, new UpdateThingAction(gameController, this));
        getProtocolHandler().addAction(PlaceThingInRoomRequestDTO.class, new PlaceThingInRoomAction(gameController, this));
        getProtocolHandler().addAction(UpdateThingLocationDto.class, new UpdateThingLocationAction(gameController, this));

        getProtocolHandler().addAction(ConnectRequestDTO.class, new ConnectAction(gameController, this));
        getProtocolHandler().addAction(GetExitDTO.class, new GetExitAction(gameController, this));
        getProtocolHandler().addAction(MovementRequestDTO.class, new MoveAction(gameController, this));
        getProtocolHandler().addAction(UseExitRequestDTO.class, new UseExitAction(gameController, this));
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
            case USERLEFTROOM:
                handleUserLeftRoom((UserLeftRoomDTO) event.getExtraData());
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

    private void handleUserLeftRoom(UserLeftRoomDTO data) {
        Location currentUserLocation = gameController.getGameState().getLocation(currentUser.getId());
        Long currentRoomId = currentUserLocation.getRoom().getId();
        if (!currentUser.getId().equals( data.getUserId() ) ) {
            if ( currentRoomId.equals(data.getOldRoomId())) {
                send(data);
            } else if ( currentRoomId.equals(data.getNewRoomId())) {
                User user = gameController.getGameState().findUserById(data.getUserId());
                Location location = gameController.getGameState().getLocation(user.getId());
                UserEnteredRoomDTO dto = DTOFactory.getInstance().getUserEnteredRoomDTO(user, location, data.isTeleported());
                send(dto);
            }
        }
    }

    private void thingPlaced(User user, Thing thing) {
        ThingDto thingDto = DTOFactory.getInstance().getThingDTO(thing);
        ThingPlacedDTO dto = new ThingPlacedDTO(user.getId(), thingDto);
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
        if (!userDto.getId().equals(currentUser.getId())) {
            sendAvatar(user);
        }
        UserUpdatedDTO userUpdatedDTO = new UserUpdatedDTO(userDto);
        send(userUpdatedDTO);
    }

    public void send(ProtocolDTO dto) {
        if (clientSender != null && clientSender.isRunning()) {
            clientSender.send(dto);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void sendGameState() {
        // Send Verbs
        sendVerbs(currentUser);
        // Send Tiles
        sendTiles(currentUser);
        // Send Avatars
        sendAvatars(currentUser);
        // Rooms
        sendRooms(currentUser);
        // Send Things
        sendThings(currentUser);
        // Logged in Users;
        for (User u : gameController.getGameState().getOnlineUsers()) {
            if (!u.getId().equals(currentUser.getId())) {
                UserOnlineDTO userOnlineDTO = new UserOnlineDTO(u.getId(), u.getName());
                send(userOnlineDTO);
            }
        }
    }

    public void sendThingsInRoom() {
        Location currentUserLocation = gameController.getGameState().getLocation(currentUser.getId());
        Room room = currentUserLocation.getRoom();
        for (Thing thing : ThingFactory.getInstance(STORIMMicroServer.DATADIR).getAllThingsInRoom(room.getId())) {
            ThingDto thingDto = DTOFactory.getInstance().getThingDTO(thing);
            RoomDto roomDto = DTOFactory.getInstance().getRoomDto(room);
            ThingInRoomDTO thingInRoomDTO = new ThingInRoomDTO(thingDto, roomDto);
            send(thingInRoomDTO);
        }
    }

    private void sendThings(User user) {
        for (Thing thing : ThingFactory.getInstance(STORIMMicroServer.DATADIR).getThings(user.getId())) {
            sendThing(thing);
        }
    }

    public void sendThing(Thing thing) {
        ThingDto thingDto = DTOFactory.getInstance().getThingDTO(thing);
        GetThingResultDTO getThingResultDTO = new GetThingResultDTO(true, null, thingDto);
        send(getThingResultDTO);
    }

    private void sendAvatars(User User) {
        for (Avatar avatar : AvatarFactory.getInstance(STORIMMicroServer.DATADIR).getAvatars(User)) {
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


    private void sendTiles(User User) {
        for (Tile tile : TileFactory.getInstance(STORIMMicroServer.DATADIR).geTiles(User)) {
            sendTile(tile.getId());
        }
    }

    private void sendRooms(User user) {
        for (Room room : Database.getInstance().getAll(Room.class, user.getId())) {
            sendRoom(room);
        }
    }

    private void sendVerbs(User user) {
        HashMap<Long, String> verbs = server.getDataServerConnection().getVerbs(user.getId());
        UserVerbsDTO userVerbsDTO = new UserVerbsDTO(verbs);
        send(userVerbsDTO);
    }

    private void avatarAdded(Avatar avatar) {
        AvatarDto avatarDto = DTOFactory.getInstance().getAvatarDTO(avatar);
        if (avatar.getOwnerId().equals(currentUser.getId())) {
            AvatarAddedDTO avatarAddedDTO = DTOFactory.getInstance().getAvatarAddedDTO(avatarDto);
            send(avatarAddedDTO);
        }
    }

    private void thingAdded(Thing thing) {
        ThingDto thingDto = DTOFactory.getInstance().getThingDTO(thing);
        if (thing.getOwnerId().equals(currentUser.getId())) {
            ThingAddedDTO thingAddedDTO = new ThingAddedDTO(thingDto);
            send(thingAddedDTO);
        }
    }


    private void handleRoomAdded(Room room) {
        RoomDto roomDTO = DTOFactory.getInstance().getRoomDto(room);
        if (room.getTileId() != null) {
            sendTile(room.getTileId());
        }
        RoomAddedDTO roomAddedDTO = DTOFactory.getInstance().getRoomAddedDTO(roomDTO);
        send(roomAddedDTO);
    }


    private void sendRoomUpdate(Room room) {
        RoomDto roomDTO = DTOFactory.getInstance().getRoomDto(room);
        if (room.getTileId() != null) {
            sendTile(room.getTileId());
        }
        RoomUpdatedDTO roomUpdatedDTO = DTOFactory.getInstance().getRoomUpdatedDTO(roomDTO);
        send(roomUpdatedDTO);
    }

    private void sendTile(Long tileId) {
        Tile tile = TileFactory.getInstance(STORIMMicroServer.DATADIR).findTileById(tileId);
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
        if (isInCurrentRoom(cmdResult.getCallerId())) {
            String output = "";
            if (cmdResult.getCallerId().equals(currentUser.getId())) {
                output = cmdResult.getToCaller();
            } else {
                output = cmdResult.getToLocation();
            }
            //ExecVerbResultDTO execVerbResultDTO = DTOFactory.getInstance().getExecVerbResultDto(cmdResult.getId(), output);
            //send(execVerbResultDTO);
        }
    }

    private boolean isInCurrentRoom(Long callerId) {
        Location currentUserLocation = gameController.getGameState().getLocation(currentUser.getId());
        Location callerLocation = gameController.getGameState().getLocation(callerId);
        return currentUserLocation.getRoom().getId().equals(callerLocation.getRoom().getId());
    }


    private void handleTeleported(User user, Long oldRoomId) {
        //FIXME handle teleport
//        if (user.equals(currentUser)) {
//            sendUsersInRoom(user.getLocation().getRoom());
//            sendThingsInRoom(user.getLocation().getRoom());
//        } else {
//            sendRoom(user.getLocation().getRoom());
//            if (user.getLocation().getRoom().getTileId() != null) {
//                sendTile(user.getLocation().getRoom().getTileId());
//            }
//            sendThingsInRoom(user.getLocation().getRoom());
//            LocationDto location = DTOFactory.getInstance().getLocationDTO(user.getLocation());
//           // UserTeleportedDTO userTeleportedDTO = DTOFactory.getInstance().getUserTeleportedDTO(user, oldRoomId, location);
//            //send(userTeleportedDTO);
//        }
    }

    public void sendUsersInRoom() {
        Location currentUserLocation = gameController.getGameState().getLocation(currentUser.getId());
        Room room = currentUserLocation.getRoom();
        gameController.getGameState().getOnlineUsers().forEach(user -> {
            Location location = gameController.getGameState().getLocation(user.getId());
            if (room.getId().equals(location.getRoom().getId())) {
                if ( !currentUser.getId().equals(user.getId())) {
                    sendUserInRoom(user);
                }
            }
        });
    }

    public void sendUserInRoom(User user) {
        Location location = gameController.getGameState().getLocation(user.getId());
        UserInRoomDTO dto = DTOFactory.getInstance().getUserInRoomDTO(user, location);
        send(dto);
    }

    public void handleUserLocation(User user) {
        Location location = gameController.getGameState().getLocation(user.getId());
        Location currentUserLocation = gameController.getGameState().getLocation(currentUser.getId());
        if (location.getRoom().getId().equals(currentUserLocation.getRoom().getId())) {
            LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(location);
            Long sequence = null;
            if (user.getId().equals(currentUser.getId())) sequence = location.getSequence();
            UserLocationUpdatedDTO userLocationUpdatedDTO = new UserLocationUpdatedDTO(user.getId(), locationDto, sequence);
            send(userLocationUpdatedDTO);
        }
    }

    public void sendRoom(Room room) {
        if (room.getTileId() != null) {
            sendTile(room.getTileId());
        }
        // First send the Exits
        for (Exit e : room.getExits()) {
            ExitDto exitDto = DTOFactory.getInstance().getExitDTO(e);
            GetExitResultDTO exitResultDTO = new GetExitResultDTO(true, null, exitDto);
            send(exitResultDTO);
        }
        RoomDto roomDto = DTOFactory.getInstance().getRoomDto(room);
        GetRoomResultDTO getRoomResultDTO = DTOFactory.getInstance().getRoomResultDTO(true, null, roomDto);
        send(getRoomResultDTO);
    }

    private void sendUserDetails(User user) {
        UserDto userDTO = DTOFactory.getInstance().getUserDTO(user);
        Location location = gameController.getGameState().getLocation(user.getId());
        Room room = location.getRoom();
        sendRoom(room);
        sendAvatar(user);
        GetUserResultDTO getUserResultDTO = DTOFactory.getInstance().getGetUserResultDTO(true, null, userDTO);
        send(getUserResultDTO);
    }

    private void handleUserConnected(User u) {
        if (currentUser != null && !currentUser.equals(u)) {
            sendAvatar(u);
            UserConnectedAction action = new UserConnectedAction(gameController, this, u);
            gameController.addAction(action);
        }
    }

    private void handleUserDisconnected(User u) {
        UserDisconnectedDTO dto = DTOFactory.getInstance().getUserDisconnectedDTO(u);
        send(dto);
    }


    public boolean verifyUser(Long userId, String token) {
        boolean userValid = false;
        VerifyUserRequestDTO dto = new VerifyUserRequestDTO(userId, token);
        VerifyUserResponseDTO response = (VerifyUserResponseDTO) server.getLoginServerConnection().sendReceive(dto, VerifyUserResponseDTO.class);
        if (response.getUser() != null) {
            userValid = true;
            User verifiedUser = User.valueOf(response.getUser());
            if (gameController.getGameState().findUserById(verifiedUser.getId()) != null) {
                // Remove previous from cache, start new  (reconnect in the same connection?)
                gameController.getGameState().removeOnlineUser(verifiedUser);
            }
            gameController.getGameState().addOnlineUser(verifiedUser);
            setCurrentUser(verifiedUser);
        }
        return userValid;
    }

    public void setRoom(Long roomId) {
        Room room = RoomFactory.getInstance().findRoomByID(roomId);
        if (room != null) {
            Location location = new Location(room, room.getSpawnPointX(), room.getSpawnPointY());
            gameController.getGameState().setLocation(currentUser, location);
            LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(location);
            RoomDto roomDto = DTOFactory.getInstance().getRoomDto(room);
            send(new SetRoomDTO(roomDto, locationDto));
            sendUsersInRoom();
            sendThingsInRoom();
        }
    }

    public void updateRoomForUser(User user, Location location) {
        UpdateUserRoomDto updateUserRoomDto = new UpdateUserRoomDto(user.getId(), location.getRoom().getId());
        server.getDataServerConnection().send(updateUserRoomDto);
    }
}
