package com.hamming.storim.server;


import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.dataserver.SessionDto;
import com.hamming.storim.server.common.factories.ExitFactory;
import com.hamming.storim.server.common.factories.LocationFactory;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Exit;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.*;
import com.hamming.storim.server.game.action.*;

import java.awt.image.DataBuffer;
import java.net.Socket;
import java.util.HashMap;

public class STORIMClientConnection extends ClientConnection implements RoomListener, ServerListener {

    private UserDto currentUser;
    private Room currentRoom;
    private STORIMMicroServer server;
    private GameController gameController;
    private boolean admin;

    public STORIMClientConnection(STORIMMicroServer server, String id, Socket s, GameController controller) {
        super(id, s, controller);
        this.server = server;
        controller.addServerListener(this);
        admin = false;
    }

    @Override
    public void addActions() {
        gameController = (GameController) getServerWorker();
        getProtocolHandler().addAction(new TeleportAction(gameController, this));
        getProtocolHandler().addAction(new GetRoomAction(gameController, this));
        getProtocolHandler().addAction(new GetRoomsAction(gameController, this));
        getProtocolHandler().addAction(new GetUserAction(gameController, this));
        getProtocolHandler().addAction(new GetVerbAction(gameController, this));
        getProtocolHandler().addAction(new ExecVerbAction(gameController, this));
        getProtocolHandler().addAction(new AddVerbAction(gameController, this));
        getProtocolHandler().addAction(new UpdateVerbAction(gameController, this));
        getProtocolHandler().addAction(new UpdateUserAction(gameController, this));
        getProtocolHandler().addAction(new UpdateAvatarAction(gameController, this));
        getProtocolHandler().addAction(new AddThingAction(gameController, this));
        getProtocolHandler().addAction(new DeleteThingAction(gameController, this));
        getProtocolHandler().addAction(new UpdateThingAction(gameController, this));
        getProtocolHandler().addAction(new PlaceThingInRoomAction(gameController, this));
        getProtocolHandler().addAction(new UpdateThingLocationAction(gameController, this));
        getProtocolHandler().addAction(new ConnectAction(gameController, this));
        getProtocolHandler().addAction(new GetExitAction(gameController, this));
        getProtocolHandler().addAction(new MoveAction(gameController, this));
        getProtocolHandler().addAction(new UseExitAction(gameController, this));
        getProtocolHandler().addAction(new DeleteVerbAction(gameController, this));
        getProtocolHandler().addAction(new GetAvatarsAction(this));
        getProtocolHandler().addAction(new AddAvatarAction(this));
        getProtocolHandler().addAction(new GetAvatarAction(this));
        getProtocolHandler().addAction(new DeleteAvatarAction(gameController, this));
        getProtocolHandler().addAction(new SetAvatarAction(gameController, this));
        getProtocolHandler().addAction(new AddRoomAction(gameController, this));
        getProtocolHandler().addAction(new GetRoomsForUserAction(this));
        getProtocolHandler().addAction(new UpdateRoomAction(gameController, this));
        getProtocolHandler().addAction(new DeleteRoomAction(gameController, this));
        getProtocolHandler().addAction(new GetTilesForUserAction(this));
        getProtocolHandler().addAction(new GetTileAction(this));
        getProtocolHandler().addAction(new AddThingAction(gameController, this));
        getProtocolHandler().addAction(new GetThingsForUserAction(this));
        getProtocolHandler().addAction(new GetThingAction(this));
        getProtocolHandler().addAction(new AddExitAction(gameController, this));
        getProtocolHandler().addAction(new DeleteExitAction(gameController, this));
        getProtocolHandler().addAction(new UpdateExitAction(gameController, this));
        getProtocolHandler().addAction(new UpdateExitLocationAction(gameController, this));
        getProtocolHandler().addAction(new LoginAction(gameController, this));
        getProtocolHandler().addAction(new VerifyAdminAction(gameController, this));
        getProtocolHandler().addAction(new GetUsersAction(gameController, this));
        getProtocolHandler().addAction(new UpdateUserAction(gameController, this));
        getProtocolHandler().addAction(new AddUserAction(gameController, this));
        getProtocolHandler().addAction(new DeleteUserAction(gameController, this));
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
        Location newLocation = gameController.getGameState().getUserLocation(user.getId());
        Room newRoom = RoomFactory.getInstance().findRoomByID(newLocation.getRoomId());
        UserLeftRoomDTO userLeftRoomDTO = new UserLeftRoomDTO(user.getId(), user.getName(), newRoom.getId(), newRoom.getName());
        send(userLeftRoomDTO);
    }

    private void userEnteredRoom(UserDto user, Long oldRoomId) {
        Room oldRoom = RoomFactory.getInstance().findRoomByID(oldRoomId);
        Location newLocation = gameController.getGameState().getUserLocation(user.getId());
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

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
    }

    public void sendGameState() {
        // Send Verbs
        sendVerbs(currentUser);
        // Send Avatars
        //sendAvatars(currentUser); Niet doen. Client vraagt
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
        if (avatar != null) {
            sendAvatar(avatar);
        }
    }

    public void sendThingsInRoom(Room room) {
        for (Long thingId : room.getObjectsInRoom()) {
            ThingDto thing = server.getUserDataServerProxy().getThing(thingId);
            LocationDto locationDto = server.getUserDataServerProxy().getLocation(thingId);
            if (thing != null) {
                ThingInRoomDTO thingInRoomDTO = new ThingInRoomDTO(thing, locationDto);
                send(thingInRoomDTO);
            } else {
                Logger.info(this, "Cannot get Thing:" + thingId);
            }
        }
    }


    public void sendAvatar(AvatarDto avatar) {
        GetAvatarResponseDTO getAvatarResultDTO = new GetAvatarResponseDTO(avatar);
        send(getAvatarResultDTO);
    }

    public void sendAvatar(UserDto user) {
        if (user.getCurrentAvatarID() != null) {
            AvatarDto avatarDto = server.getUserDataServerProxy().getAvatar(user.getCurrentAvatarID());
            if (avatarDto != null) {
                AvatarSetDTO avatarSetDTO = new AvatarSetDTO(user.getId(), avatarDto);
                send(avatarSetDTO);
            }
        }
    }

    private void sendVerbs(UserDto user) {
        HashMap<Long, String> verbs = server.getUserDataServerProxy().getVerbs(user.getId());
        UserVerbsDTO userVerbsDTO = new UserVerbsDTO(verbs);
        send(userVerbsDTO);
    }

    private void thingAdded(ThingDto thing) {
        ThingAddedDTO thingAddedDTO = new ThingAddedDTO(thing);
        send(thingAddedDTO);
    }


    private void handleRoomAdded(Room room) {
        RoomDto roomDTO = DTOFactory.getInstance().getRoomDto(room, server.getServerURI());
        RoomAddedDTO roomAddedDTO = new RoomAddedDTO(roomDTO);
        send(roomAddedDTO);
    }

    private void sendRoomUpdate(Room room) {
        RoomDto roomDTO = DTOFactory.getInstance().getRoomDto(room, server.getServerURI());
        RoomUpdatedDTO roomUpdatedDTO = new RoomUpdatedDTO(roomDTO);
        send(roomUpdatedDTO);
    }

    private void handleRoomDeleted(Room room) {
        send(new RoomDeletedDTO(room.getId()));
    }


    public void sendUsersInRoom(Room room) {
        gameController.getGameState().getOnlineUsers().forEach(user -> {
            Location location = gameController.getGameState().getUserLocation(user.getId());
            if (room.getId().equals(location.getRoomId())) {
                if (!currentUser.getId().equals(user.getId())) {
                    sendUserInRoom(user);
                }
            }
        });
    }

    public void sendUserInRoom(UserDto user) {
        Location location = gameController.getGameState().getUserLocation(user.getId());
        UserInRoomDTO dto = DTOFactory.getInstance().getUserInRoomDTO(user, location);
        send(dto);
        if (user.getCurrentAvatarID() != null) {
            //Send Avatar
            AvatarDto avatarDto = server.getUserDataServerProxy().getAvatar(user.getCurrentAvatarID());
            AvatarSetDTO avatarSetDTO = new AvatarSetDTO(user.getId(), avatarDto);
            send(avatarSetDTO);
        }
    }

    public void userLocationUpdate(UserDto user) {
        Location location = gameController.getGameState().getUserLocation(user.getId());
        LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(location);
        LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO(LocationUpdateDTO.Type.USER, user.getId(), locationDto);
        send(locationUpdateDTO);
    }

    public void sendRoom(Room room) {
        RoomDto roomDto = DTOFactory.getInstance().getRoomDto(room, server.getServerURI());
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

    private void thingPlaced(ThingDto thing, UserDto user) {
        LocationDto locationDto = getServer().getUserDataServerProxy().getLocation(thing.getId());
        ThingInRoomDTO thingInRoomDTO = new ThingInRoomDTO(thing, locationDto);
        send(thingInRoomDTO);
        String toLocation = user.getName() + " places " + thing.getName();
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(user.getId(), MessageInRoomDTO.Type.USER, toLocation);
        send(messageInRoomDTO);
    }

    public UserDto verifyUserToken(Long userId, String token) {
        UserDto verifiedUser = server.getUserDataServerProxy().verifyUserToken(getId(), userId, token);
        return verifiedUser;
    }

    public void setRoom(Long roomId) {
        currentRoom = RoomFactory.getInstance().findRoomByID(roomId);
        if (currentRoom != null) {
            String serverName = server.getServerName();
            int x = currentRoom.getSpawnPointX();
            int y = currentRoom.getSpawnPointY();
            LocationDto lastKnownLocation = getServer().getUserDataServerProxy().getLocation(currentUser.getId());
            if (lastKnownLocation != null && lastKnownLocation.getRoomId().equals(roomId)) {
                x = lastKnownLocation.getX();
                y = lastKnownLocation.getY();
            }

            Location location = gameController.getGameState().getUserLocation(currentUser.getId());
            if ( location != null ) {
                location.setServerId(serverName);
                location.setRoomId(currentRoom.getId());
                location.setX(x);
                location.setY(y);
            } else {
                location = new Location(currentUser.getId(), serverName, currentRoom.getId(), x, y);
            }

            gameController.getGameState().setUserLocation(currentUser, location);
            RoomDto roomDto = DTOFactory.getInstance().getRoomDto(currentRoom, server.getServerURI());
            send(new SetRoomDTO(roomDto));
            sendUsersInRoom(currentRoom);
            sendThingsInRoom(currentRoom);
            sendExitsInRoom(currentRoom);
        }
    }

    private void sendExitsInRoom(Room room) {
        for (Exit exit : room.getExits()) {
            ExitDto exitDto = DTOFactory.getInstance().getExitDTO(exit);
            ExitInRoomDTO exitInRoomDTO = new ExitInRoomDTO(exitDto);
            send(exitInRoomDTO);
        }
    }

    public STORIMMicroServer getServer() {
        return server;
    }

    @Override
    public void connected() {

    }

    public void currentUserConnected(UserDto currentUser, Long roomId) {
        setCurrentUser(currentUser);
        sendGameState();
        Room room = null;
        Location location = null;
        LocationDto locationDto = null;


        if ( roomId != null ) {
            // Connect to a specific room given by StorimURI
            room = RoomFactory.getInstance().findRoomByID(roomId);
            if (room != null) {
                int x = room.getSpawnPointX();
                int y = room.getSpawnPointY();
                locationDto = new LocationDto(-1l, getServer().getServerURI(), room.getId(), x, y);
                location = LocationFactory.getInstance().createLocation(currentUser.getId(), locationDto);
            }
        } else {
            // Check for a location stored in dataserver
            locationDto = getServer().getUserDataServerProxy().getLocation(currentUser.getId());
            if (locationDto != null) {
                room = RoomFactory.getInstance().findRoomByID(locationDto.getRoomId());
                if (room != null) {
                    location = LocationFactory.getInstance().createLocation(currentUser.getId(), locationDto);
                }
            }
        }



        if (location == null) {
            Logger.info(this, "User '" + currentUser.getId() + "' location not found or corrupt, using default Room (1)");
            room = RoomFactory.getInstance().findRoomByID(1L);
            int x = room.getSpawnPointX();
            int y = room.getSpawnPointY();
            locationDto = new LocationDto(-1l, getServer().getServerURI(), room.getId(), x, y);
            location = LocationFactory.getInstance().createLocation(currentUser.getId(), locationDto);
        }

        getServer().getUserDataServerProxy().setLocation(currentUser.getId(), locationDto);
        gameController.getGameState().setUserLocation(currentUser, location);

        setRoom(room.getId());
        // Send current User info
        SetCurrentUserDTO setCurrentUserDTO = new SetCurrentUserDTO(currentUser, locationDto);
        send(setCurrentUserDTO);
        if (currentUser.getCurrentAvatarID() != null) {
            //Send Avatar
            AvatarDto avatarDto = getServer().getUserDataServerProxy().getAvatar(currentUser.getCurrentAvatarID());
            AvatarSetDTO avatarSetDTO = new AvatarSetDTO(currentUser.getId(), avatarDto);
            send(avatarSetDTO);
        }
        // Add this user as online user
        gameController.getGameState().getOnlineUsers().add(currentUser);
        // RegisterListener for the current Room
        gameController.addRoomListener(location.getRoomId(), this);
        // Notify the listeners
        gameController.fireServerEvent(this, new ServerEvent(ServerEvent.Type.USERCONNECTED, currentUser));

    }

    @Override
    public void disconnected() {
        gameController.removeServerListener(this);
        Location location = gameController.getGameState().getUserLocation(currentUser.getId());
        gameController.removeRoomListener(location.getRoomId(), this);
        LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(location);
        server.getUserDataServerProxy().setLocation(currentUser.getId(), locationDto);
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
            case THINGUPDATED:
                thingUpdated((ThingDto) event.getData(), (UserDto) event.getExtraData());
                break;
            case EXITUPDATED:
                exitUpdated((ExitDto) event.getData(), (UserDto) event.getExtraData());
                break;
            case THINGPLACED:
                thingPlaced((ThingDto) event.getData(), (UserDto) event.getExtraData());
                break;
            case THINGLOCATIONUPDATE:
                thingLocationUpdate((LocationDto) event.getData(), (UserDto) event.getExtraData());
                break;
            case EXITLOCATIONUPDATE:
                exitLocationUpdate((ExitDto) event.getData(), (UserDto) event.getExtraData());
                break;
            case EXITADDED:
                exitAdded((ExitDto) event.getData());
                break;
            case ROOMUPDATED:
                //TODO
                break;
            case MESSAGEINROOM:
                messageInROom(event.getData(), (String) event.getExtraData());
                break;
        }
    }

    private void exitAdded(ExitDto exitDto) {
        ExitInRoomDTO exitInRoomDTO = new ExitInRoomDTO(exitDto);
        send(exitInRoomDTO);
    }

    private void exitUpdated(ExitDto exitDto, UserDto user) {
        ExitUpdatedDTO exitUpdatedDTO = new ExitUpdatedDTO(exitDto);
        send(exitUpdatedDTO);

        String txt = user.getName() + " changes exit " + exitDto.getName();
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(exitDto.getId(), MessageInRoomDTO.Type.USER, txt);
        send(messageInRoomDTO);
    }

    private void thingUpdated(ThingDto thing, UserDto user) {
        ThingUpdatedDTO thingUpdatedDTO = new ThingUpdatedDTO(thing);
        send(thingUpdatedDTO);

        String txt = user.getName() + " changes " + thing.getName();
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(thing.getId(), MessageInRoomDTO.Type.USER, txt);
        send(messageInRoomDTO);
    }

    private void thingLocationUpdate(LocationDto locationDto, UserDto user) {
        LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO(LocationUpdateDTO.Type.THING, locationDto.getObjectId(), locationDto);
        ThingDto thingDto = getServer().getUserDataServerProxy().getThing(locationDto.getObjectId());
        send(locationUpdateDTO);
        String txt = user.getName() + " moves " + thingDto.getName();
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(locationDto.getObjectId(), MessageInRoomDTO.Type.USER, txt);
        send(messageInRoomDTO);
    }

    private void exitLocationUpdate(ExitDto exitDto, UserDto user) {
        ExitLocationUpdatedDTO exitLocationUpdatedDTO = new ExitLocationUpdatedDTO(exitDto.getId(), exitDto.getX(), exitDto.getY());
        send(exitLocationUpdatedDTO);
        Exit exit = ExitFactory.getInstance().findExitById(exitDto.getId());
        String txt = user.getName() + " moves exit " + exit.getName();
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(exitDto.getId(), MessageInRoomDTO.Type.USER, txt);
        send(messageInRoomDTO);
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

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }
}
