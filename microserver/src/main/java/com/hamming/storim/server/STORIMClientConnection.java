package com.hamming.storim.server;


import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarResponseDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomResultDTO;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.factories.ExitFactory;
import com.hamming.storim.server.common.factories.LocationFactory;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.factories.TileSetFactory;
import com.hamming.storim.server.common.model.Exit;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.common.model.TileSet;
import com.hamming.storim.server.game.*;
import com.hamming.storim.server.game.action.*;

import java.net.Socket;
import java.util.HashMap;

public class STORIMClientConnection extends ClientConnection implements RoomListener, ServerListener {

    private Room currentRoom;
    private STORIMMicroServer server;
    private GameController gameController;
    private RoomAuthorisationListener roomAuthorisationListener;
    private TileSetAuthorisationListener tileSetAuthorisationListener;
    private boolean userAdmin = false;

    public STORIMClientConnection(STORIMMicroServer server, String id, Socket s, GameController controller) {
        super(id, s, controller);
        this.server = server;
        controller.addServerListener(this);
        setupAuthorisationListeners();
    }

    private void setupAuthorisationListeners() {
        roomAuthorisationListener = new RoomAuthorisationListener(server, this);
        tileSetAuthorisationListener = new TileSetAuthorisationListener(server, this);
        server.getAuthorisationController().addAuthorisationListener(roomAuthorisationListener);
        server.getAuthorisationController().addAuthorisationListener(tileSetAuthorisationListener);
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
        getProtocolHandler().addAction(new DeleteThingAction(this));
        getProtocolHandler().addAction(new UpdateThingAction(gameController, this));
        getProtocolHandler().addAction(new PlaceThingInRoomAction(gameController, this));
        getProtocolHandler().addAction(new UpdateThingLocationAction(gameController, this));
        getProtocolHandler().addAction(new LoginWithTokenAction(gameController, this));
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
        getProtocolHandler().addAction(new GetUsersAction(gameController, this));
        getProtocolHandler().addAction(new UpdateUserAction(gameController, this));
        getProtocolHandler().addAction(new AddUserAction(gameController, this));
        getProtocolHandler().addAction(new DeleteUserAction(gameController, this));
        getProtocolHandler().addAction(new GetTileSetAction(this));
        getProtocolHandler().addAction(new GetTileSetsAction(this));
        getProtocolHandler().addAction(new AddTileSetAction(this));
        getProtocolHandler().addAction(new UpdateTileSetAction(this));
        getProtocolHandler().addAction(new DeleteTileSetAction(gameController, this));
        getProtocolHandler().addAction(new GetTilesSetsForUserAction(this));
        getProtocolHandler().addAction(new SearchUsersAction(gameController, this));
        getProtocolHandler().addAction(new UpdateAuthorisationAction(this));
        getProtocolHandler().addAction(new GetServerConfigurationAction(gameController,this));
        getProtocolHandler().addAction(new UpdateServerConfigurationAction(gameController,this));
    }


    private void avatarSet(UserDto user, AvatarDto avatar) {
        AvatarSetDTO avatarSetDTO = new AvatarSetDTO(user.getId(), avatar);
        send(avatarSetDTO);
    }

    private void messageInROom(MessageInRoomDTO dto, String message) {
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(dto.getSourceID(), dto.getSourceType(), message, dto.getMessageType());
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

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void sendGameState() {
        // Send Verbs
        sendVerbs(getCurrentUser());
        // Logged in Users;
        for (UserDto u : gameController.getGameState().getOnlineUsers()) {
            if (!u.getId().equals(getCurrentUser().getId())) {
                UserOnlineDTO userOnlineDTO = new UserOnlineDTO(u.getId(), u.getName());
                send(userOnlineDTO);
            }
        }
    }


    public void sendThingsInRoom(Room room) {
        try {
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
        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName() + ".sendThingsInRoom", e.getMessage());
            send(errorDTO);
        }
    }


    public void sendAvatar(AvatarDto avatar) {
        GetAvatarResponseDTO getAvatarResultDTO = new GetAvatarResponseDTO(true, avatar, null);
        send(getAvatarResultDTO);
    }

    public void sendAvatar(UserDto user) {
        try {
            if (user.getCurrentAvatarID() != null) {
                AvatarDto avatarDto = server.getUserDataServerProxy().getAvatar(user.getCurrentAvatarID());
                if (avatarDto != null) {
                    AvatarSetDTO avatarSetDTO = new AvatarSetDTO(user.getId(), avatarDto);
                    send(avatarSetDTO);
                }
            }
        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName() + ".sendAvatar", e.getMessage());
            send(errorDTO);
        }
    }

    private void sendVerbs(UserDto user) {
        try {
            HashMap<Long, String> verbs = server.getUserDataServerProxy().getVerbs(user.getId());
            UserVerbsDTO userVerbsDTO = new UserVerbsDTO(verbs);
            send(userVerbsDTO);
        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName() + ".sendVerbs", e.getMessage());
            send(errorDTO);
        }
    }

    private void thingAdded(ThingDto thing) {
        ThingAddedDTO thingAddedDTO = new ThingAddedDTO(thing);
        send(thingAddedDTO);
    }


    private void handleRoomAdded(Room room) {
        RoomDto roomDTO = DTOFactory.getInstance().getRoomDto(room, server.getServerURI().getServerURL(), isAuthorized(room));
        RoomAddedDTO roomAddedDTO = new RoomAddedDTO(roomDTO);
        send(roomAddedDTO);
    }

    public void sendUsersInRoom(Room room) {
        gameController.getGameState().getOnlineUsers().forEach(user -> {
            Location location = gameController.getGameState().getUserLocation(user.getId());
            if (room.getId().equals(location.getRoomId())) {
                if (!getCurrentUser().getId().equals(user.getId())) {
                    sendUserInRoom(user);
                }
            }
        });
    }

    public void sendUserInRoom(UserDto user) {
        try {
            Location location = gameController.getGameState().getUserLocation(user.getId());
            UserInRoomDTO dto = DTOFactory.getInstance().getUserInRoomDTO(user, location);
            send(dto);
            if (user.getCurrentAvatarID() != null) {
                //Send Avatar
                AvatarDto avatarDto = server.getUserDataServerProxy().getAvatar(user.getCurrentAvatarID());
                AvatarSetDTO avatarSetDTO = new AvatarSetDTO(user.getId(), avatarDto);
                send(avatarSetDTO);
            }
        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName() + ".sendUserInRoom", e.getMessage());
            send(errorDTO);
        }
    }

    public void userLocationUpdate(UserDto user) {
        Location location = gameController.getGameState().getUserLocation(user.getId());
        LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(location);
        LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO(LocationUpdateDTO.Type.USER, user.getId(), locationDto);
        send(locationUpdateDTO);
    }

    public void sendRoom(Room room) {
        RoomDto roomDto = DTOFactory.getInstance().getRoomDto(room, server.getServerURI().getServerURL(), isAuthorized(room));
        GetRoomResultDTO getRoomResultDTO = new GetRoomResultDTO(true, roomDto, null);
        send(getRoomResultDTO);
    }

    private void userConnected(UserDto u) {
        if (getCurrentUser() != null && !getCurrentUser().equals(u)) {
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
        try {
            LocationDto locationDto = getServer().getUserDataServerProxy().getLocation(thing.getId());
            ThingInRoomDTO thingInRoomDTO = new ThingInRoomDTO(thing, locationDto);
            send(thingInRoomDTO);
            String toLocation = user.getName() + " places " + thing.getName();
            MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(user.getId(), MessageInRoomDTO.sType.USER, toLocation, MessageInRoomDTO.mType.MOVE);
            send(messageInRoomDTO);
        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName() + ".thingPlaced", e.getMessage());
            send(errorDTO);
        }
    }

    public void setRoom(Long roomId) {
        try {
            if ( currentRoom != null ) {
                gameController.removeRoomListener(currentRoom.getId(), this);
            }
            currentRoom = RoomFactory.getInstance().findRoomByID(roomId);
            if (currentRoom != null) {
                String serverName = server.getServerName();
                int x = 0;
                int y = 0;
                LocationDto lastKnownLocation = getServer().getUserDataServerProxy().getLocation(getCurrentUser().getId());
                if (lastKnownLocation != null && lastKnownLocation.getRoomId().equals(roomId)) {
                    x = lastKnownLocation.getX();
                    y = lastKnownLocation.getY();
                } else {
                    x = rowToX(currentRoom.getSpawnRow(), currentRoom);
                    y = colToY(currentRoom.getSpawnCol(), currentRoom);
                }

                Location location = gameController.getGameState().getUserLocation(getCurrentUser().getId());
                if (location != null) {
                    location.setServerId(serverName);
                    location.setRoomId(currentRoom.getId());
                    location.setX(x);
                    location.setY(y);
                } else {
                    location = new Location(getCurrentUser().getId(), serverName, currentRoom.getId(), x, y);
                }

                gameController.getGameState().setUserLocation(getCurrentUser(), location);
                RoomDto roomDto = DTOFactory.getInstance().getRoomDto(currentRoom, server.getServerURI().getServerURL(), isAuthorized(currentRoom));
                send(new SetRoomDTO(roomDto));
                sendUsersInRoom(currentRoom);
                sendThingsInRoom(currentRoom);
                sendExitsInRoom(currentRoom);
                gameController.addRoomListener(currentRoom.getId(), this);
            }
        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName() + ".setRoom", e.getMessage());
            send(errorDTO);
        }
    }

    public int rowToX(int row, Room room) {
        TileSet tileSet = TileSetFactory.getInstance().findTileSetById(room.getBackTileSetId());
        int x = 0;
        if ( row <= room.getRows() ) {
            x = row * tileSet.getTileWidth();
        }
        return x;
    }

    private int colToY(int col, Room room) {
        TileSet tileSet = TileSetFactory.getInstance().findTileSetById(room.getBackTileSetId());
        int y = 0;
        if ( col <= room.getCols() ) {
            y =  col * tileSet.getTileHeight();
        }
        return y;
    }

    private int calculateSpawnX(TileSet tileSet) {
        return currentRoom.getSpawnCol() * tileSet.getTileWidth();
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

    public void currentUserConnected(UserDto userDto, Long roomId) {
        setCurrentUser(userDto);
        sendGameState();
        Room room = null;
        Location location = null;
        LocationDto locationDto = null;
        try {
            if (roomId != null) {
                // Connect to a specific room given by StorimURI
                room = RoomFactory.getInstance().findRoomByID(roomId);
                if (room != null) {
                    TileSet tileSet = TileSetFactory.getInstance().findTileSetById(room.getBackTileSetId());
                    int x = room.getSpawnCol() * tileSet.getTileWidth();
                    int y = room.getSpawnRow() * tileSet.getTileHeight();
                    locationDto = new LocationDto(-1l, getServer().getServerURI().getServerURL(), room.getId(), x, y);
                    location = LocationFactory.getInstance().createLocation(getCurrentUser().getId(), locationDto);
                }
            } else {
                // Check for a location stored in dataserver
                locationDto = getServer().getUserDataServerProxy().getLocation(getCurrentUser().getId());
                if (locationDto != null) {
                    room = RoomFactory.getInstance().findRoomByID(locationDto.getRoomId());
                    if (room != null) {
                        location = LocationFactory.getInstance().createLocation(getCurrentUser().getId(), locationDto);
                    }
                }
            }
            if (location == null) {
                Logger.info(this, "User '" + getCurrentUser().getId() + "' location not found or corrupt, using default Room from ServerConfiguration");
                room = server.getServerConfiguration().getDefaultRoom();
                TileSet tileSet = TileSetFactory.getInstance().findTileSetById(room.getBackTileSetId());
                int x = room.getSpawnCol() * tileSet.getTileWidth();
                int y = room.getSpawnRow() * tileSet.getTileHeight();
                locationDto = new LocationDto(-1l, getServer().getServerURI().getServerURL(), room.getId(), x, y);
                location = LocationFactory.getInstance().createLocation(getCurrentUser().getId(), locationDto);
            }

            getServer().getUserDataServerProxy().setLocation(getCurrentUser().getId(), locationDto);
            gameController.getGameState().setUserLocation(getCurrentUser(), location);

            setRoom(room.getId());
            // Send current User info
            SetCurrentUserDTO setCurrentUserDTO = new SetCurrentUserDTO(getCurrentUser(), locationDto);
            send(setCurrentUserDTO);
            if (getCurrentUser().getCurrentAvatarID() != null) {
                //Send Avatar
                AvatarDto avatarDto = getServer().getUserDataServerProxy().getAvatar(getCurrentUser().getCurrentAvatarID());
                AvatarSetDTO avatarSetDTO = new AvatarSetDTO(getCurrentUser().getId(), avatarDto);
                send(avatarSetDTO);
            }
            // Add this user as online user
            gameController.getGameState().getOnlineUsers().add(getCurrentUser());
            // Notify the listeners
            gameController.fireServerEvent(this, new ServerEvent(ServerEvent.Type.USERCONNECTED, getCurrentUser()));

        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName() + ".getCurrentUser()Connected", e.getMessage());
            send(errorDTO);
        }


    }

    @Override
    public void disconnected() {
        gameController.removeServerListener(this);
        server.getAuthorisationController().removeAuthorisationListener(roomAuthorisationListener);
        server.getAuthorisationController().removeAuthorisationListener(tileSetAuthorisationListener);
        if (getCurrentUser() != null) {
            Location location = gameController.getGameState().getUserLocation(getCurrentUser().getId());
            gameController.removeRoomListener(location.getRoomId(), this);
            LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(location);
            server.getUserDataServerProxy().setLocation(getCurrentUser().getId(), locationDto);
            UserDisconnectedAction action = new UserDisconnectedAction(gameController, this, getCurrentUser());
            gameController.addAction(action);
            setCurrentUser(null);
        }
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
                roomUpdated((RoomDto) event.getData());
                break;
            case MESSAGEINROOM:
                MessageInRoomDTO messageInRoomDTO = (MessageInRoomDTO) event.getData();
                messageInROom(messageInRoomDTO, (String) event.getExtraData());
                break;
        }
    }

    private void roomUpdated(RoomDto roomDto) {
        RoomUpdatedDTO roomUpdatedDTO = new RoomUpdatedDTO(roomDto);
        send(roomUpdatedDTO);
    }

    private void exitAdded(ExitDto exitDto) {
        ExitInRoomDTO exitInRoomDTO = new ExitInRoomDTO(exitDto);
        send(exitInRoomDTO);
    }

    private void exitUpdated(ExitDto exitDto, UserDto user) {
        ExitUpdatedDTO exitUpdatedDTO = new ExitUpdatedDTO(exitDto);
        send(exitUpdatedDTO);

        String txt = user.getName() + " changes exit " + exitDto.getName();
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(exitDto.getId(), MessageInRoomDTO.sType.USER, txt, MessageInRoomDTO.mType.UPDATE);
        send(messageInRoomDTO);
    }

    private void thingUpdated(ThingDto thing, UserDto user) {
        ThingUpdatedDTO thingUpdatedDTO = new ThingUpdatedDTO(thing);
        send(thingUpdatedDTO);

        String txt = user.getName() + " changes " + thing.getName();
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(thing.getId(), MessageInRoomDTO.sType.USER, txt, MessageInRoomDTO.mType.UPDATE);
        send(messageInRoomDTO);
    }

    private void thingLocationUpdate(LocationDto locationDto, UserDto user) {
        try {
            LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO(LocationUpdateDTO.Type.THING, locationDto.getObjectId(), locationDto);
            ThingDto thingDto = getServer().getUserDataServerProxy().getThing(locationDto.getObjectId());
            send(locationUpdateDTO);
            String txt = user.getName() + " moves " + thingDto.getName();
            MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(locationDto.getObjectId(), MessageInRoomDTO.sType.USER, txt, MessageInRoomDTO.mType.MOVE);
            send(messageInRoomDTO);
        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName() + ".thingLocationUpdate", e.getMessage());
            send(errorDTO);
        }
    }

    private void exitLocationUpdate(ExitDto exitDto, UserDto user) {
        ExitLocationUpdatedDTO exitLocationUpdatedDTO = new ExitLocationUpdatedDTO(exitDto.getId(), exitDto.getX(), exitDto.getY());
        send(exitLocationUpdatedDTO);
        Exit exit = ExitFactory.getInstance().findExitById(exitDto.getId());
        String txt = user.getName() + " moves exit " + exit.getName();
        MessageInRoomDTO messageInRoomDTO = new MessageInRoomDTO(exitDto.getId(), MessageInRoomDTO.sType.USER, txt, MessageInRoomDTO.mType.MOVE);
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

    public boolean isUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(boolean userAdmin) {
        this.userAdmin = userAdmin;
    }
}
