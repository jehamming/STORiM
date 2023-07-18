package com.hamming.storim.client.controller;

import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.view.GameViewPanel;
import com.hamming.storim.common.CalcTools;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.request.MovementRequestDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateExitLocationDto;
import com.hamming.storim.common.dto.protocol.request.UpdateThingLocationDto;
import com.hamming.storim.common.dto.protocol.request.UseExitRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileResultDTO;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.dto.protocol.serverpush.ExitAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.RoomUpdatedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ThingInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ThingUpdatedDTO;
import com.hamming.storim.common.interfaces.*;
import com.hamming.storim.common.net.ProtocolReceiver;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.common.view.ViewListener;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

public class GameViewController implements ConnectionListener {

    private ConnectionController connectionController;
    private GameViewPanel gameView;
    private long sequenceNumber;
    private List<MovementRequestDTO> movementRequests;
    private LocationDto lastReceivedLocation;
    private List<ViewListener> viewListeners;
    private STORIMWindow storimWindow;
    private UserDto currentUser;

    private RoomDto currentRoom;
    private AvatarDto currentUserAvatar;

    public GameViewController(STORIMWindow storimWindow, GameViewPanel gameView, ConnectionController connectionController) {
        this.storimWindow = storimWindow;
        this.connectionController = connectionController;
        this.gameView = gameView;
        connectionController.addConnectionListener(this);
        sequenceNumber = 0;
        movementRequests = new ArrayList<>();
        viewListeners = new ArrayList<>();

        storimWindow.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                windowResized();
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        registerReceivers();
    }

    private void windowResized() {
        gameView.scheduleAction(() -> gameView.componentResized());
    }

    private void registerReceivers() {
        connectionController.registerReceiver(SetRoomDTO.class, (ProtocolReceiver<SetRoomDTO>) dto -> setRoom(dto.getRoom()));
        connectionController.registerReceiver(UserInRoomDTO.class, (ProtocolReceiver<UserInRoomDTO>) dto -> userInRoom(dto));
        connectionController.registerReceiver(LocationUpdateDTO.class, (ProtocolReceiver<LocationUpdateDTO>) dto -> locationUpdate(dto));
        connectionController.registerReceiver(UserDisconnectedDTO.class, (ProtocolReceiver<UserDisconnectedDTO>) dto -> userDisconnected(dto));
        connectionController.registerReceiver(UserLeftRoomDTO.class, (ProtocolReceiver<UserLeftRoomDTO>) dto -> userLeftRoom(dto));
        connectionController.registerReceiver(UserEnteredRoomDTO.class, (ProtocolReceiver<UserEnteredRoomDTO>) dto -> userEnteredRoom(dto));
        connectionController.registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto));
        connectionController.registerReceiver(AvatarSetDTO.class, (ProtocolReceiver<AvatarSetDTO>) dto -> setAvatar(dto));
        connectionController.registerReceiver(RoomUpdatedDTO.class, (ProtocolReceiver<RoomUpdatedDTO>) dto -> roomUpdated(dto));
        connectionController.registerReceiver(ThingInRoomDTO.class, (ProtocolReceiver<ThingInRoomDTO>) dto -> addThing(dto));
        connectionController.registerReceiver(ThingUpdatedDTO.class, (ProtocolReceiver<ThingUpdatedDTO>) dto -> updateThing(dto.getThing()));
        connectionController.registerReceiver(ExitAddedDTO.class, (ProtocolReceiver<ExitAddedDTO>) dto -> addExit(dto.getExitDto()));
        connectionController.registerReceiver(ExitInRoomDTO.class, (ProtocolReceiver<ExitInRoomDTO>) dto -> addExit(dto.getExitDto()));
        connectionController.registerReceiver(ExitDeletedDTO.class, (ProtocolReceiver<ExitDeletedDTO>) dto -> deleteExit(dto.getExitID()));
        connectionController.registerReceiver(ExitUpdatedDTO.class, (ProtocolReceiver<ExitUpdatedDTO>) dto -> updateExit(dto.getExitDto()));
        connectionController.registerReceiver(ExitLocationUpdatedDTO.class, (ProtocolReceiver<ExitLocationUpdatedDTO>) dto -> updateExitLocation(dto.getExitId(), dto.getX(), dto.getY()));
    }

    private void updateExitLocation(Long exitId, int x, int y) {
        gameView.scheduleAction(() -> gameView.setExitLocation(exitId, x, y));
    }

    private void updateExit(ExitDto exitDto) {
        gameView.scheduleAction(() -> gameView.updateExit(exitDto));
    }

    private void deleteExit(Long exitID) {
        gameView.scheduleAction(() -> gameView.deleteExit(exitID));
    }

    private void addExit(ExitDto exitDto) {
        gameView.scheduleAction(() -> gameView.addExit(exitDto, storimWindow.getCurrentServerId()));
        gameView.scheduleAction(() -> gameView.setExitLocation(exitDto.getId(), exitDto.getX(), exitDto.getY()));
    }

    private void updateThing(ThingDto thing) {
        gameView.scheduleAction(() -> gameView.updateThing(thing));
    }

    private void addThing(ThingInRoomDTO dto) {
        gameView.scheduleAction(() -> gameView.addThing(dto.getThing()));
        Long id = dto.getThing().getId();
        int x = dto.getLocation().getX();
        int y = dto.getLocation().getY();
        gameView.scheduleAction(() -> gameView.setThingLocation(id, x, y));
    }


    private void roomUpdated(RoomUpdatedDTO dto) {
        if (lastReceivedLocation != null && lastReceivedLocation.getRoomId().equals(dto.getRoom().getId())) {
            updateRoom(dto.getRoom());
        }
    }

    private void setAvatar(AvatarSetDTO dto) {
        Long userId = dto.getUserId();
        AvatarDto avatarDto = dto.getAvatar();
        gameView.scheduleAction(() -> gameView.setAvatar(userId, avatarDto));
        if (currentUser != null && currentUser.getId().equals(userId)) {
            currentUserAvatar = dto.getAvatar();
        }
    }


    private void userLeftRoom(UserLeftRoomDTO dto) {
        gameView.scheduleAction(() -> gameView.removePlayer(dto.getUserId()));
    }

    private void userDisconnected(UserDisconnectedDTO dto) {
        gameView.scheduleAction(() -> gameView.removePlayer(dto.getUserID()));
    }

    private void locationUpdate(LocationUpdateDTO dto) {
        LocationDto l = dto.getLocation();
        switch (dto.getType()) {
            case USER:
                if (dto.getObjectId().equals(storimWindow.getCurrentUser().getId())) {
                    moveCurrentUser(dto.getSequenceNumber(), dto.getLocation());
                } else {
                    gameView.scheduleAction(() -> gameView.setPlayerLocation(dto.getObjectId(), l.getX(), l.getY()));
                }
                break;
            case THING:
                gameView.scheduleAction(() -> gameView.setThingLocation(dto.getObjectId(), l.getX(), l.getY()));
                break;
        }
    }

    // The method uses client side prediction to counter lag.
    private void moveCurrentUser(Long sequenceNumber, LocationDto l) {
        if (sequenceNumber == null) {
            resetRequests();
            sequenceNumber = 0L;
            lastReceivedLocation = l;
        }
        // First : Set the location based on the server respons (server = authoritive
        gameView.scheduleAction(() -> gameView.setPlayerLocation(currentUser.getId(), l.getX(), l.getY()));
        // Remove all the request before this sequence (if any)
        deleteRequestsUpTO(sequenceNumber);
        // Apply all the requests that server has not processed yet.
        applyMoveRequests(l);
    }

    private void userInRoom(UserInRoomDTO dto) {
        UserDto user = dto.getUser();
        LocationDto location = dto.getLocation();
        if (user.getId().equals(storimWindow.getCurrentUser().getId())) {
            lastReceivedLocation = location;
        }
        if (user.getCurrentAvatarID() != null) {
            gameView.scheduleAction(() -> gameView.addPlayer(user.getId(), user.getName(), null));
        } else {
            gameView.scheduleAction(() -> gameView.addPlayer(user.getId(), user.getName(), null));
        }
        gameView.scheduleAction(() -> gameView.setPlayerLocation(user.getId(), location.getX(), location.getY()));
    }

    private void setCurrentUser(SetCurrentUserDTO dto) {
        currentUser = dto.getUser();
        LocationDto location = dto.getLocation();
        lastReceivedLocation = location;
        if (currentUser.getCurrentAvatarID() != null) {
            gameView.scheduleAction(() -> gameView.addPlayer(currentUser.getId(), currentUser.getName(), null));
        } else {
            gameView.scheduleAction(() -> gameView.addPlayer(currentUser.getId(), currentUser.getName(), null));
        }
        gameView.scheduleAction(() -> gameView.setPlayerLocation(currentUser.getId(), location.getX(), location.getY()));
    }


    private void userEnteredRoom(UserEnteredRoomDTO dto) {
        UserDto user = dto.getUser();
        LocationDto location = dto.getLocation();
        if (user.getCurrentAvatarID() != null) {
            //FIXME get Avatar
            //  byte[] imageData = controllers.getUserController().getAvatar(user.getCurrentAvatarID()).getImageData();
            //gameView.scheduleAction(() -> gameView.addPlayer(user.getId(), user.getName(), imageData));
            gameView.scheduleAction(() -> gameView.addPlayer(user.getId(), user.getName(), null));
        } else {
            gameView.scheduleAction(() -> gameView.addPlayer(user.getId(), user.getName(), null));
        }
        gameView.scheduleAction(() -> gameView.setPlayerLocation(user.getId(), location.getX(), location.getY()));
    }

    private void updateRoom(RoomDto room) {
        if (room.getTileID() != null) {
            TileDto tile = getTile(room.getTileID());
            if (tile != null) {
                gameView.scheduleAction(() -> gameView.setTile(tile));
            }
        }
        gameView.scheduleAction(() -> gameView.setRoom(room));

    }

    private void setRoom(RoomDto room) {
        currentRoom = room;
        gameView.scheduleAction(() -> gameView.resetView());
        resetRequests();

        updateRoom(room);

        if (currentUser != null) {
            final byte[] imageData = currentUserAvatar != null ? currentUserAvatar.getImageData() : null;
            gameView.scheduleAction(() -> gameView.addPlayer(currentUser.getId(), currentUser.getName(), imageData));
        }

    }

    private TileDto getTile(Long tileId) {
        GetTileResultDTO response = connectionController.sendReceive(new GetTileDTO(tileId), GetTileResultDTO.class);
        return response.getTile();
    }

    private void resetRequests() {
        sequenceNumber = 0;
        synchronized (movementRequests) {
            movementRequests = new ArrayList<>();
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
        gameView.scheduleAction(() -> gameView.setPlayerLocation(storimWindow.getCurrentUser().getId(), newLocation.getX(), newLocation.getY()));
        Logger.info(this, "ScheduledMove-Sequence:" + dto.getSequence() + "-" + newLocation.getX() + "," + newLocation.getY() + ",");
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
            connectionController.send(requestDTO);
        }
    }


    private long getNextSequenceNumber() {
        return sequenceNumber++;
    }


    @Override
    public void connected() {
        gameView.scheduleAction(() -> gameView.resetView());
    }

    @Override
    public void disconnected() {
        gameView.scheduleAction(() -> gameView.resetView());
        lastReceivedLocation = null;
        sequenceNumber = 0;
        currentUserAvatar = null;
        currentUser = null;
        currentRoom = null;
        resetRequests();
        gameView.scheduleAction(() -> gameView.removePlayer(storimWindow.getCurrentUser().getId()));
    }

    public void updateThingLocationRequest(Long thingId, int x, int y) {
        UpdateThingLocationDto updateThingLocationDto = new UpdateThingLocationDto(thingId, x, y);
        connectionController.send(updateThingLocationDto);
    }

    public void exitClicked(Long id, String name, String roomURI) {
        if ( roomURI == null ) {
            int result = JOptionPane.showConfirmDialog(storimWindow, "Use exit '" + name + "'?", "Use exit", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                connectionController.send(new UseExitRequestDTO(id));
            }
        } else {
            // To another server
            int result = JOptionPane.showConfirmDialog(storimWindow, "Use exit '" + roomURI + "' are you sure?", "Use exit", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                storimWindow.useExitToOtherServer(roomURI);
            }

        }
    }

    public void updateExitLocationRequest(Long exitID, int x, int y) {
        UpdateExitLocationDto updateExitLocationDto = new UpdateExitLocationDto(exitID,currentRoom.getId(), x, y);
        connectionController.send(updateExitLocationDto);
    }
}
