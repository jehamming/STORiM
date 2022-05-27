package com.hamming.storim.server;


import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.request.*;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.dto.protocol.serverpush.old.*;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.UpdateUserRoomDto;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Exit;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.*;
import com.hamming.storim.server.game.action.*;

import java.net.Socket;
import java.util.HashMap;

public class STORIMClientConnection extends ClientConnection implements RoomListener, ServerListener {

    private UserDto currentUser;
    private STORIMMicroServer server;
    private GameController gameController;

    public STORIMClientConnection(STORIMMicroServer server, String id, Socket s, GameController controller) {
        super(id, s, controller);
        this.server = server;
        controller.addServerListener(this);
    }

    @Override
    public String getClientId() {
        String clientId = this.getClass().getSimpleName();
        if ( currentUser != null ) {
            clientId = clientId.concat("-UserId:"+ currentUser.getId() );
        }
        return clientId;
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
        getProtocolHandler().addAction(UpdateUserDto.class, new UpdateUserAction(gameController, this));
        getProtocolHandler().addAction(UpdateAvatarDto.class, new UpdateAvatarAction(gameController, this));
        getProtocolHandler().addAction(AddThingDto.class, new AddThingAction(gameController, this));
        getProtocolHandler().addAction(DeleteThingDTO.class, new DeleteThingAction(gameController, this));
        getProtocolHandler().addAction(UpdateThingDto.class, new UpdateThingAction(gameController, this));
        getProtocolHandler().addAction(PlaceThingInRoomRequestDTO.class, new PlaceThingInRoomAction(gameController, this));
        getProtocolHandler().addAction(UpdateThingLocationDto.class, new UpdateThingLocationAction(gameController, this));
        getProtocolHandler().addAction(ConnectDTO.class, new ConnectAction(gameController, this));
        getProtocolHandler().addAction(GetExitDTO.class, new GetExitAction(gameController, this));
        getProtocolHandler().addAction(MovementRequestDTO.class, new MoveAction(gameController, this));
        getProtocolHandler().addAction(UseExitRequestDTO.class, new UseExitAction(gameController, this));
        getProtocolHandler().addAction(DeleteVerbDTO.class, new DeleteVerbAction(gameController, this));
        getProtocolHandler().addAction(GetAvatarsDTO.class, new GetAvatarsAction(this));
        getProtocolHandler().addAction(AddAvatarDto.class, new AddAvatarAction( this));
        getProtocolHandler().addAction(GetAvatarDTO.class, new GetAvatarAction(this));
        getProtocolHandler().addAction(DeleteAvatarDTO.class, new DeleteAvatarAction(gameController, this));
        getProtocolHandler().addAction(SetAvatarDto.class, new SetAvatarAction(gameController, this));
        getProtocolHandler().addAction(AddRoomDto.class, new AddRoomAction(gameController, this));
        getProtocolHandler().addAction(GetRoomsForUserDTO.class, new GetRoomsForUserAction(this));
        getProtocolHandler().addAction(UpdateRoomDto.class, new UpdateRoomAction(gameController, this));
        getProtocolHandler().addAction(DeleteRoomDTO.class, new DeleteRoomAction(gameController, this));
        getProtocolHandler().addAction(GetTilesForUserDTO.class, new GetTilesForUserAction(this));
        getProtocolHandler().addAction(GetTileDTO.class, new GetTileAction(this));
    }


    private void avatarSet(UserDto user, AvatarDto avatar) {
        AvatarSetDTO avatarSetDTO = new AvatarSetDTO(user.getId(), avatar);
        send(avatarSetDTO);
    }

    private void messageInROom(DTO source, String message) {
        MessageInRoomDTO messageInRoomDTO = null;
        if (source instanceof UserDto) {
            messageInRoomDTO = new MessageInRoomDTO(source.getId(), MessageInRoomDTO.Type.USER, message);
        } else if (source instanceof ThingDto) {
            messageInRoomDTO = new MessageInRoomDTO(source.getId(), MessageInRoomDTO.Type.THING, message);
        }
        send(messageInRoomDTO);
    }

    private void userLeftRoom(UserDto user) {
        Location newLocation = gameController.getGameState().getLocation(user.getId());
        Room newRoom = newLocation.getRoom();
        UserLeftRoomDTO userLeftRoomDTO = new UserLeftRoomDTO(user.getId(), user.getName(), newRoom.getId(), newRoom.getName());
        send(userLeftRoomDTO);
    }

    private void userEnteredRoom(UserDto user, Long oldRoomId) {
        Room oldRoom = RoomFactory.getInstance().findRoomByID(oldRoomId);
        Location newLocation = gameController.getGameState().getLocation(user.getId());
        LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(newLocation);
        UserEnteredRoomDTO userEnteredRoomDTO = new UserEnteredRoomDTO(user, locationDto, oldRoomId, oldRoom.getName());
        send(userEnteredRoomDTO);
        //Send User Avatar
        sendAvatar(user);
    }



    private void avatarUpdated(AvatarDto avatar) {
        AvatarUpdatedDTO avatarUpdatedDTO = new AvatarUpdatedDTO(avatar);
        send(avatarUpdatedDTO);
    }

    private void avatarDeleted(AvatarDto avatar) {
        AvatarDeletedDTO avatarDeletedDTO = new AvatarDeletedDTO(avatar.getId());
        send(avatarDeletedDTO);
    }

    private void userUpdated(UserDto user) {
        UserUpdatedDTO userUpdatedDTO = new UserUpdatedDTO(user);
        send(userUpdatedDTO);
    }

    public UserDto getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
    }

    public void sendGameState() {
        // Send Verbs
        sendVerbs(currentUser);
        // Send Avatars
        sendAvatars(currentUser);
        // Rooms
        sendRooms(currentUser);
        // Send Things
       // sendThings(currentUser);
        // Logged in Users;
        for (UserDto u : gameController.getGameState().getOnlineUsers()) {
            if (!u.getId().equals(currentUser.getId())) {
                UserOnlineDTO userOnlineDTO = new UserOnlineDTO(u.getId(), u.getName());
                send(userOnlineDTO);
            }
        }
    }

    private void sendAvatars(UserDto user) {
            for (Long avatarId : server.getUserDataServerProxy().getAvatars(user.getId())) {
                sendAvatar(avatarId);
            }
    }

    private void sendAvatar(Long avatarId) {
        AvatarDto avatar = server.getUserDataServerProxy().getAvatar(avatarId);
        if ( avatar != null ) {
            sendAvatar(avatar);
        }
    }

    public void sendThingsInRoom() {
        Location currentUserLocation = gameController.getGameState().getLocation(currentUser.getId());
        Room room = currentUserLocation.getRoom();
        //FIXME Things
//        for (ThingDto thing : ThingFactory.getInstance(STORIMMicroServer.DATADIR).getAllThingsInRoom(room.getId())) {
//            RoomDto roomDto = DTOFactory.getInstance().getRoomDto(room);
//            ThingInRoomDTO thingInRoomDTO = new ThingInRoomDTO(thing, roomDto);
//            send(thingInRoomDTO);
//        }
    }



    public void sendAvatar(AvatarDto avatar) {
        GetAvatarResponseDTO getAvatarResultDTO = new GetAvatarResponseDTO(avatar);
        send(getAvatarResultDTO);
    }

    public void sendAvatar(UserDto user) {
        if (user.getCurrentAvatarID() != null) {
            AvatarDto avatarDto = server.getUserDataServerProxy().getAvatar(user.getCurrentAvatarID());
            if ( avatarDto != null ) {
                AvatarSetDTO avatarSetDTO = new AvatarSetDTO(user.getId(), avatarDto);
                send(avatarSetDTO);
            }
        }
    }


    private void sendRooms(UserDto user) {
        for (Room room : Database.getInstance().getAll(Room.class, user.getId())) {
            sendRoom(room);
        }
    }

    private void sendVerbs(UserDto user) {
        HashMap<Long, String> verbs =  server.getUserDataServerProxy().getVerbs(user.getId());
        UserVerbsDTO userVerbsDTO = new UserVerbsDTO(verbs);
        send(userVerbsDTO);
    }

    private void thingAdded(ThingDto thing) {
            ThingAddedDTO thingAddedDTO = new ThingAddedDTO(thing);
            send(thingAddedDTO);
    }


    private void handleRoomAdded(Room room) {
        RoomDto roomDTO = DTOFactory.getInstance().getRoomDto(room);
        RoomAddedDTO roomAddedDTO = new RoomAddedDTO(roomDTO);
        send(roomAddedDTO);
    }

    private void sendRoomUpdate(Room room) {
        RoomDto roomDTO = DTOFactory.getInstance().getRoomDto(room);
        RoomUpdatedDTO roomUpdatedDTO = new RoomUpdatedDTO(roomDTO);
        send(roomUpdatedDTO);
    }

    private void handleRoomDeleted(Room room) {
        send(new RoomDeletedDTO(room.getId()));
    }



    public void sendUsersInRoom() {
        Location currentUserLocation = gameController.getGameState().getLocation(currentUser.getId());
        Room room = currentUserLocation.getRoom();
        gameController.getGameState().getOnlineUsers().forEach(user -> {
            Location location = gameController.getGameState().getLocation(user.getId());
            if (room.getId().equals(location.getRoom().getId())) {
                if (!currentUser.getId().equals(user.getId())) {
                    sendUserInRoom(user);
                }
            }
        });
    }

    public void sendUserInRoom(UserDto user) {
        Location location = gameController.getGameState().getLocation(user.getId());
        UserInRoomDTO dto = DTOFactory.getInstance().getUserInRoomDTO(user, location);
        send(dto);
        if ( user.getCurrentAvatarID() != null ) {
            //Send Avatar
            AvatarDto avatarDto =  server.getUserDataServerProxy().getAvatar(user.getCurrentAvatarID());
            AvatarSetDTO avatarSetDTO = new AvatarSetDTO(user.getId(), avatarDto);
            send(avatarSetDTO);
        }
    }

    public void userLocationUpdate(UserDto user) {
        Location location = gameController.getGameState().getLocation(user.getId());
        LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(location);
        Long sequence = null;
        if (user.getId().equals(currentUser.getId())) sequence = location.getSequence();
        UserLocationUpdatedDTO userLocationUpdatedDTO = new UserLocationUpdatedDTO(user.getId(), locationDto, sequence);
        send(userLocationUpdatedDTO);
    }

    public void sendRoom(Room room) {
        // First send the Exits
        for (Exit e : room.getExits()) {
            ExitDto exitDto = DTOFactory.getInstance().getExitDTO(e);
            GetExitResponseDTO exitResultDTO = new GetExitResponseDTO(true, null, exitDto);
            send(exitResultDTO);
        }
        RoomDto roomDto = DTOFactory.getInstance().getRoomDto(room);
        GetRoomResultDTO getRoomResultDTO = new GetRoomResultDTO(roomDto);
        send(getRoomResultDTO);
    }

    private void userConnected(UserDto u) {
        if (currentUser != null && !currentUser.equals(u)) {
            sendAvatar(u);
            UserConnectedAction action = new UserConnectedAction(gameController, this, u);
            gameController.addAction(action);
        }
    }

    private void userDisconnected(UserDto u) {
        UserDisconnectedDTO dto = new UserDisconnectedDTO(u.getId(), u.getName());
        send(dto);
    }


    public UserDto verifyUser(Long userId, String token) {
        UserDto verifiedUser = server.getLoginServerProxy().verifyUser(userId, token);
        return verifiedUser;
    }

    public void setRoom(Long roomId) {
        Room room = RoomFactory.getInstance().findRoomByID(roomId);
        if (room != null) {
            Location location = new Location(room, room.getSpawnPointX(), room.getSpawnPointY());
            gameController.getGameState().setLocation(currentUser, location);
            RoomDto roomDto = DTOFactory.getInstance().getRoomDto(room);
            send(new SetRoomDTO(roomDto));
            sendUsersInRoom();
            sendThingsInRoom();
        }
    }

    public void updateRoomForUser(UserDto user, Location location) {
        UpdateUserRoomDto updateUserRoomDto = new UpdateUserRoomDto(user.getId(), location.getRoom().getId());
        server.getUserDataServerProxy().send(updateUserRoomDto);
    }

    public STORIMMicroServer getServer() {
        return server;
    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
        gameController.removeServerListener(this);
        Location location = gameController.getGameState().getLocation(currentUser.getId());
        gameController.removeRoomListener(location.getRoom().getId(), this);
        UserDisconnectedAction action = new UserDisconnectedAction(gameController, this, currentUser);
        gameController.addAction(action);
        currentUser = null;
    }

    @Override
    public void roomEvent(RoomEvent event) {
        switch (event.getType()) {
            case AVATARSET:
                avatarSet((UserDto) event.getData(), (AvatarDto) event.getExtraData());
                break;
            case USERUPDATED:
                userUpdated((UserDto) event.getData());
                break;
            case USERLEFTROOM:
                userLeftRoom((UserDto) event.getData());
                break;
            case USERENTEREDROOM:
                userEnteredRoom((UserDto) event.getData(), (Long) event.getExtraData());
                break;
            case USERLOCATIONUPDATE:
                userLocationUpdate((UserDto) event.getData());
                break;
            case THINGPLACED:
                //TODO
                break;
            case THINGLOCATIONUPDATE:
                //TODO
                break;
            case ROOMUPDATED:
                //TODO
                break;
            case MESSAGEINROOM:
                messageInROom(event.getData(), (String) event.getExtraData());
                break;
        }
    }

    @Override
    public void serverEvent(ServerEvent event) {
        switch (event.getType()) {
            case USERCONNECTED:
                userConnected((UserDto) event.getData());
                break;
            case USERDISCONNECTED:
                userDisconnected((UserDto) event.getData());
                break;
        }
    }

}
