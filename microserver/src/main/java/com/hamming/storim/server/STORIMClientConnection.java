package com.hamming.storim.server;


import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.factories.ExitFactory;
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

    private Room currentRoom;
    private STORIMMicroServer server;
    private GameController gameController;

    public STORIMClientConnection(STORIMMicroServer server, String id, Socket s, GameController controller) {
        super(id, s, controller);
        this.server = server;
        controller.addServerListener(this);
    }

    @Override
    public void addActions() {
        gameController = (GameController) getServerWorker();
        getProtocolHandler().addAction(new TeleportAction(gameController, this));
        getProtocolHandler().addAction(new GetRoomAction(gameController, this));
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
        getProtocolHandler().addAction(new AddAvatarAction( this));
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
        getProtocolHandler().addAction(new GetServersAction(this));
        getProtocolHandler().addAction(new GetRoomsForServerAction(this));
        getProtocolHandler().addAction(new AddExitAction( gameController,this));
        getProtocolHandler().addAction(new DeleteExitAction( gameController,this));
        getProtocolHandler().addAction(new UpdateExitAction( gameController,this));
        getProtocolHandler().addAction(new UpdateExitLocationAction( gameController,this));
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
        Room newRoom =  RoomFactory.getInstance().findRoomByID(newLocation.getRoomId());
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

    public void sendThingsInRoom(Room room) {
        for (Long thingId : room.getObjectsInRoom() ) {
            ThingDto thing = server.getUserDataServerProxy().getThing(thingId);
            LocationDto locationDto = server.getUserDataServerProxy().getLocation(thingId);
            if ( thing != null ) {
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
        if ( user.getCurrentAvatarID() != null ) {
            //Send Avatar
            AvatarDto avatarDto =  server.getUserDataServerProxy().getAvatar(user.getCurrentAvatarID());
            AvatarSetDTO avatarSetDTO = new AvatarSetDTO(user.getId(), avatarDto);
            send(avatarSetDTO);
        }
    }

    public void userLocationUpdate(UserDto user) {
        Location location = gameController.getGameState().getUserLocation(user.getId());
        LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(location);
        LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO(user.getId(), locationDto);
        send(locationUpdateDTO);
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

    private void thingPlaced(ThingDto thing, UserDto user) {
        LocationDto locationDto = getServer().getUserDataServerProxy().getLocation(thing.getId());
        ThingInRoomDTO thingInRoomDTO = new ThingInRoomDTO(thing, locationDto );
        send(thingInRoomDTO);
        String toLocation = user.getName()+" places " + thing.getName();
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(user.getId(), MessageInRoomDTO.Type.USER, toLocation);
        send(messageInRoomDTO);
    }

    public UserDto verifyUser(Long userId, String token) {
        UserDto verifiedUser = server.getLoginServerProxy().verifyUser(userId, token);
        return verifiedUser;
    }

    public void setRoom(Long roomId) {
        currentRoom = RoomFactory.getInstance().findRoomByID(roomId);
        if (currentRoom != null) {
            String serverName = server.getServerName();
            int x = currentRoom.getSpawnPointX();
            int y = currentRoom.getSpawnPointY();
            LocationDto lastKnownLocation = getServer().getUserDataServerProxy().getLocation(currentUser.getId());
            if ( lastKnownLocation != null && lastKnownLocation.getRoomId().equals(roomId)) {
                x = lastKnownLocation.getX();
                y = lastKnownLocation.getY();
            }
            Location location = new Location(currentUser.getId(), serverName, currentRoom.getId(), x , y);
            gameController.getGameState().setUserLocation(currentUser, location);
            RoomDto roomDto = DTOFactory.getInstance().getRoomDto(currentRoom);
            send(new SetRoomDTO(roomDto));
            sendUsersInRoom(currentRoom);
            sendThingsInRoom(currentRoom);
            sendExitsInRoom(currentRoom);
        }
    }

    private void sendExitsInRoom(Room room) {
        for (Exit exit: room.getExits()) {
            ExitDto exitDto = DTOFactory.getInstance().getExitDTO(exit);
            LocationDto locationDto = getServer().getUserDataServerProxy().getLocation(exit.getId());
            ExitInRoomDTO exitInRoomDTO = new ExitInRoomDTO(exitDto, locationDto);
            send(exitInRoomDTO);
        }
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
                exitLocationUpdate((LocationDto) event.getData(), (UserDto) event.getExtraData());
                break;
            case EXITADDED:
                exitAdded((ExitDto) event.getData(), (LocationDto) event.getExtraData());
                break;
            case ROOMUPDATED:
                //TODO
                break;
            case MESSAGEINROOM:
                messageInROom(event.getData(), (String) event.getExtraData());
                break;
        }
    }

    private void exitAdded(ExitDto exitDto, LocationDto locationDto) {
        ExitInRoomDTO exitInRoomDTO = new ExitInRoomDTO(exitDto, locationDto);
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
        LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO(locationDto.getObjectId(), locationDto);
        ThingDto thingDto = getServer().getUserDataServerProxy().getThing(locationDto.getObjectId());
        send(locationUpdateDTO);
        String txt = user.getName() + " moves " + thingDto.getName();
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(locationDto.getObjectId(), MessageInRoomDTO.Type.USER, txt);
        send(messageInRoomDTO);
    }

    private void exitLocationUpdate(LocationDto locationDto, UserDto user) {
        LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO(locationDto.getObjectId(), locationDto);
        send(locationUpdateDTO);
        Exit exit = ExitFactory.getInstance().findExitById(locationDto.getObjectId());
        String txt = user.getName() + " moves exit " + exit.getName();
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(locationDto.getObjectId(), MessageInRoomDTO.Type.USER, txt);
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

}
