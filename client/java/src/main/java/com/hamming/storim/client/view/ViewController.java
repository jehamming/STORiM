package com.hamming.storim.client.view;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.common.CalcTools;
import com.hamming.storim.common.Controllers;
import com.hamming.storim.common.ProtocolHandler;
import com.hamming.storim.common.interfaces.*;
import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.MovementRequestDTO;
import com.hamming.storim.common.dto.protocol.thing.UpdateThingLocationDto;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewController implements ViewerController, ConnectionListener, UserListener, RoomListener, RoomUpdateListener, ThingListener {

    private ProtocolHandler protocolHandler;
    private Controllers controllers;
    private GameView gameView;
    private long sequenceNumber;
    private List<MovementRequestDTO> movementRequests;
    private LocationDto lastReceivedLocation;
    private Long currentUserid;
    private List<ViewListener> viewListeners;

    public ViewController(GameView gameView, Controllers controllers) {
        this.controllers = controllers;
        this.gameView = gameView;
        protocolHandler = new ProtocolHandler();
        controllers.getConnectionController().addConnectionListener(this);
        controllers.getUserController().addUserListener(this);
        controllers.getRoomController().addRoomListener(this);
        controllers.getRoomController().addRoomUpdateListener(this);
        controllers.getThingController().addThingListener(this);
        sequenceNumber = 0;
        movementRequests = new ArrayList<>();
        viewListeners = new ArrayList<>();
    }

     @Override
     public void addViewListener(ViewListener l) {
        viewListeners.add(l);
     }

    // The method uses client side prediction to counter lag.
    private void moveCurrentUser(Long sequenceNumber, LocationDto l) {
        UserDto user = controllers.getUserController().getCurrentUser();
        // First : Set the location based on the server respons (server = authoritive
        gameView.scheduleSetUserLocation(user.getId(), l.getX(), l.getY());
        // Remove all the request before this sequence (if any)
        deleteRequestsUpTO(sequenceNumber);
        // Apply all the requests that server has not processed yet.
        applyMoveRequests(l);
    }

    private String generateTitle(LocationDto l) {
        String title = "Room:" + l.getRoomId() + "-X:" + Math.round(l.getX()) + ",Y:" + Math.round(l.getY());
        return title;
    }

    private void resetRequests() {
        synchronized (movementRequests) {
            movementRequests = movementRequests = new ArrayList<MovementRequestDTO>();
        }
    }

    private void deleteRequestsUpTO(long sequence) {
        synchronized (movementRequests) {
            List<MovementRequestDTO> removeCollection = new ArrayList<MovementRequestDTO>();
            for (MovementRequestDTO request : movementRequests) {
                if (request.getSequence() <= sequence) {
                    removeCollection.add(request);
                }

            }
            movementRequests.removeAll(removeCollection);
        }
    }

    public void applyMoveRequests(LocationDto loc) {
        LocationDto locationToCalculateOn = loc;
        synchronized (movementRequests) {
            for (MovementRequestDTO dto : movementRequests) {
                locationToCalculateOn = applyMoveRequest(dto, locationToCalculateOn);
            }
        }
    }

    public LocationDto applyMoveRequest(MovementRequestDTO dto, LocationDto loc) {
        LocationDto newLocation = CalcTools.calculateNewPosition(dto, loc);
        gameView.scheduleSetUserLocation(controllers.getUserController().getCurrentUser().getId(), newLocation.getX(), newLocation.getY());
        System.out.println(this.getClass().getName() + "-Scheduled-" + dto.getSequence() + "-" + newLocation.getX() + "," + newLocation.getY() + ",");
        return newLocation;
    }


    private MovementRequestDTO getCurrentMoveRequest(boolean forward, boolean back, boolean left, boolean right) {
        MovementRequestDTO dto = null;
        if (forward || back || left || right) {
            dto = new MovementRequestDTO(getNextSequenceNumber(), forward, back, left, right);
            synchronized (movementRequests) {
                movementRequests.add(dto);
            }
            applyMoveRequest(dto, lastReceivedLocation);
        }
        return dto;
    }

    public void sendMoveRequest(boolean forward, boolean backward, boolean left, boolean right) {
        MovementRequestDTO requestDTO = getCurrentMoveRequest(forward, backward, left, right);
        if (requestDTO != null) {
            controllers.getConnectionController().send(requestDTO);
        }
    }


    private long getNextSequenceNumber() {
        return sequenceNumber++;
    }


    @Override
    public void connected() {
        gameView.scheduleResetView();
    }

    @Override
    public void disconnected() {
        gameView.scheduleResetView();
        lastReceivedLocation = null;
        sequenceNumber = 0;
        movementRequests = new ArrayList<MovementRequestDTO>();
        gameView.scheduleRemovePlayer(currentUserid);
    }

    @Override
    public void userConnected(UserDto user) {
        // No actions in here
    }

    @Override
    public void userUpdated(UserDto user) {
        AvatarDto avatar = null;
        if (user.getCurrentAvatarID() != null) {
            avatar = controllers.getUserController().getAvatar(user.getCurrentAvatarID());
        }
        gameView.scheduleUpdateUser(user, avatar);
    }

    @Override
    public void userDisconnected(UserDto user) {
        gameView.scheduleRemovePlayer(user.getId());
    }

    @Override
    public void userOnline(UserDto user) {
    }

    @Override
    public void loginResult(boolean success, String message) {
        if (success) {
            UserDto currentUser = controllers.getUserController().getCurrentUser();
            LocationDto location = controllers.getUserController().getUserLocation(currentUser.getId());
            lastReceivedLocation = location;
            RoomDto room = controllers.getRoomController().findRoomByID(location.getRoomId());
            Image image = null;
            if (currentUser.getCurrentAvatarID() != null) {
                byte[] imageData = controllers.getUserController().getAvatar(currentUser.getCurrentAvatarID()).getImageData();
                image = ImageUtils.decode(imageData);
            }
            gameView.scheduleAddPlayer(currentUser.getId(), currentUser.getName(), image);
            currentUserid = currentUser.getId();
            setRoom(room, location);
        }
    }

    @Override
    public void userTeleported(Long userId, LocationDto location) {

    }

    @Override
    public void avatarAdded(AvatarDto avatar) {

    }

    @Override
    public void avatarDeleted(AvatarDto avatar) {
        UserDto user = controllers.getUserController().getCurrentUser();
        if (user.getCurrentAvatarID() != null && user.getCurrentAvatarID().equals(avatar.getId())) {
            gameView.scheduleDeleteAvatar(user.getId());
        }
    }

    @Override
    public void avatarUpdated(AvatarDto avatar) {
        UserDto user = controllers.getUserController().findUserById(avatar.getOwnerID());
        if (user.getCurrentAvatarID()!= null && user.getCurrentAvatarID().equals(avatar.getId())) {
            avatar = controllers.getUserController().getAvatar(user.getCurrentAvatarID());
            gameView.scheduleUpdateUser(user, avatar);
        }
    }


    @Override
    public void userInRoom(UserDto user, LocationDto location) {
        Image image = null;
        if (user.getCurrentAvatarID() != null) {
            byte[] imageData =controllers.getUserController().getAvatar(user.getCurrentAvatarID()).getImageData();
            image = ImageUtils.decode(imageData);
        }
        gameView.scheduleAddPlayer(user.getId(), user.getName(), image);
        gameView.scheduleSetUserLocation(user.getId(), location.getX(), location.getY());
    }

    @Override
    public void userEnteredRoom(UserDto user, LocationDto location) {
        Image image = null;
        if (user.getCurrentAvatarID() != null) {
            byte[] imageData = controllers.getUserController().getAvatar(user.getCurrentAvatarID()).getImageData();
            image = ImageUtils.decode(imageData);
        }
        gameView.scheduleAddPlayer(user.getId(), user.getName(), image);
        gameView.scheduleSetUserLocation(user.getId(), location.getX(), location.getY());
    }

    @Override
    public void userLeftRoom(UserDto user) {
        gameView.scheduleRemovePlayer(user.getId());
    }

    @Override
    public void userLocationUpdate(UserDto user, LocationDto location) {
        gameView.scheduleSetUserLocation(user.getId(), location.getX(), location.getY());
    }

    @Override
    public void currentUserLocationUpdate(Long sequenceNumber, LocationDto location) {
        moveCurrentUser(sequenceNumber, location);
    }

    @Override
    public void setRoom(RoomDto room, LocationDto location) {
        gameView.scheduleResetView();
        if (room.getTileID() != null) {
            TileDto tile = controllers.getRoomController().getTile(room.getTileID());
            if (tile != null) {
                gameView.setTile(tile);
            }
        }
        gameView.scheduleSetRoom(room);
        UserDto currentUser = controllers.getUserController().getCurrentUser();
        Image image = null;
        AvatarDto avatar = controllers.getUserController().getAvatar(currentUser.getCurrentAvatarID());
        if (avatar != null) {
            image = ImageUtils.decode(avatar.getImageData());
        }
        gameView.scheduleAddPlayer(currentUser.getId(), currentUser.getName(), image);
        gameView.scheduleSetUserLocation(controllers.getUserController().getCurrentUser().getId(), location.getX(), location.getY());
        // Things
        for (ThingDto thing : controllers.getThingController().getThingsInRoom(room.getId())) {
            gameView.scheduleAddThing(thing);
        }

        resetRequests();
        lastReceivedLocation = location;
    }

    @Override
    public void thingPlacedInRoom(ThingDto thing, UserDto byUser) {
        gameView.scheduleAddThing(thing);
    }

    @Override
    public void thingRemovedFromRoom(ThingDto thing) {
        gameView.scheduleDeleteThing(thing);
    }

    @Override
    public void roomAdded(RoomDto room) {

    }

    @Override
    public void roomDeleted(RoomDto room) {

    }

    @Override
    public void roomUpdated(RoomDto room) {
        if (gameView.getRoom().getId().equals(room.getId())) {
            TileDto tile = controllers.getRoomController().getTile(room.getTileID());
            if (tile != null) {
                gameView.setTile(tile);
            }
            gameView.scheduleSetRoom(room);
        }
    }

    @Override
    public void thingAdded(ThingDto thing) {
        if ( thing.getLocation() != null ) {
            if ( thing.getLocation().getRoomId().equals(gameView.getRoom().getId())) {
                gameView.scheduleAddThing(thing);
            }
        }
    }

    @Override
    public void thingDeleted(ThingDto thing) {
        if ( thing.getLocation() != null ) {
            if ( thing.getLocation().getRoomId().equals(gameView.getRoom().getId())) {
                gameView.scheduleDeleteThing(thing);
            }
        }
    }

    @Override
    public void thingUpdated(ThingDto thing) {
        if ( thing.getLocation() != null ) {
            if ( thing.getLocation().getRoomId().equals(gameView.getRoom().getId())) {
                gameView.scheduleUpdateThing(thing);
            }
        }
    }

    public void updateThingLocationRequest(Long thingId, int x, int y) {
        UpdateThingLocationDto updateThingLocationDto = new UpdateThingLocationDto(thingId, x, y);
        controllers.getConnectionController().send(updateThingLocationDto);
    }

    public void setSelectedThing(Long id) {
        ThingDto thing = controllers.getThingController().findThingById(id);
        for (ViewListener l : viewListeners ) {
            l.thingSelectedInView(thing);
        }
    }

    public void setSelectedPlayer(Long id) {
        UserDto user = controllers.getUserController().findUserById(id);
        for (ViewListener l : viewListeners ) {
            l.userSelectedInView(user);
        }
    }
}
