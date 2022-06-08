package com.hamming.storim.client.controller;

import com.hamming.storim.client.STORIMWindow;
import com.hamming.storim.client.view.GameViewPanel;
import com.hamming.storim.common.CalcTools;
import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.request.MovementRequestDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateThingDto;
import com.hamming.storim.common.dto.protocol.request.UpdateThingLocationDto;
import com.hamming.storim.common.dto.protocol.request.UseExitRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetExitDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetExitResponseDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetTileResultDTO;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.dto.protocol.serverpush.old.RoomUpdatedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.ThingInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.ThingUpdatedDTO;
import com.hamming.storim.common.interfaces.*;
import com.hamming.storim.common.net.ProtocolReceiver;
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
        gameView.scheduleAction( () ->  gameView.componentResized());
    }

    private void registerReceivers() {
       connectionController.registerReceiver(SetRoomDTO.class, (ProtocolReceiver<SetRoomDTO>) dto -> setRoom(dto.getRoom()));
       connectionController.registerReceiver(UserInRoomDTO.class, (ProtocolReceiver<UserInRoomDTO>) dto -> userInRoom(dto));
       connectionController.registerReceiver(UserLocationUpdatedDTO.class, (ProtocolReceiver<UserLocationUpdatedDTO>) dto -> userLocationUpdate(dto));
       connectionController.registerReceiver(UserDisconnectedDTO.class, (ProtocolReceiver<UserDisconnectedDTO>) dto -> userDisconnected(dto));
       connectionController.registerReceiver(UserLeftRoomDTO.class, (ProtocolReceiver<UserLeftRoomDTO>) dto -> userLeftRoom(dto));
       connectionController.registerReceiver(UserEnteredRoomDTO.class, (ProtocolReceiver<UserEnteredRoomDTO>) dto -> userEnteredRoom(dto));
       connectionController.registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto));
       connectionController.registerReceiver(AvatarSetDTO.class, (ProtocolReceiver<AvatarSetDTO>) dto -> setAvatar(dto));
       connectionController.registerReceiver(RoomUpdatedDTO.class, (ProtocolReceiver<RoomUpdatedDTO>) dto -> roomUpdated(dto));
       connectionController.registerReceiver(ThingInRoomDTO.class, (ProtocolReceiver<ThingInRoomDTO>) dto -> addThing(dto));
       connectionController.registerReceiver(ThingUpdatedDTO.class, (ProtocolReceiver<ThingUpdatedDTO>) dto -> updateThing(dto.getThing(), dto.getLocation()));
    }

    private void updateThing(ThingDto thing, LocationDto loc) {
        gameView.scheduleAction(() -> gameView.updateThing(thing, loc));
    }

    private void addThing(ThingInRoomDTO dto) {
        gameView.scheduleAction(() -> gameView.addThing(dto.getThing(), dto.getLocation()));
    }



    private void roomUpdated(RoomUpdatedDTO dto) {
        if (lastReceivedLocation != null && lastReceivedLocation.getRoomId().equals( dto.getRoom().getId() ) ) {
            updateRoom(dto.getRoom());
        }
    }

    private void setAvatar(AvatarSetDTO dto) {
        Long userId = dto.getUserId();
        AvatarDto avatarDto = dto.getAvatar();
        gameView.scheduleAction( () -> gameView.setAvatar(userId, avatarDto));
        if (currentUser != null && currentUser.getId().equals( userId )) {
            currentUserAvatar = dto.getAvatar();
        }
    }


    private void userLeftRoom(UserLeftRoomDTO dto) {
        gameView.scheduleAction( () -> gameView.removePlayer(dto.getUserId()));
    }

    private void userDisconnected(UserDisconnectedDTO dto) {
        gameView.scheduleAction( () -> gameView.removePlayer(dto.getUserID()));
    }

    private void userLocationUpdate(UserLocationUpdatedDTO dto) {
        if (dto.getUserId().equals(storimWindow.getCurrentUser().getId())) {
            moveCurrentUser(dto.getSequenceNumber(),dto.getLocation());
        } else {
            LocationDto l = dto.getLocation();
            gameView.scheduleAction( () ->  gameView.setPlayerLocation(dto.getUserId(), l.getX(), l.getY()));
        }
    }
    // The method uses client side prediction to counter lag.
    private void moveCurrentUser(Long sequenceNumber, LocationDto l) {
        UserDto currentUser = storimWindow.getCurrentUser();
        if ( sequenceNumber == null ) {
            resetRequests();
            sequenceNumber = 0L;
            lastReceivedLocation = l;
        }
        // First : Set the location based on the server respons (server = authoritive
        gameView.scheduleAction( () ->  gameView.setPlayerLocation(currentUser.getId(), l.getX(), l.getY()));
        // Remove all the request before this sequence (if any)
        deleteRequestsUpTO(sequenceNumber);
        // Apply all the requests that server has not processed yet.
        applyMoveRequests(l);
    }

    private void userInRoom(UserInRoomDTO dto) {
        UserDto user = dto.getUser();
        LocationDto location = dto.getLocation();
        if ( user.getId().equals(storimWindow.getCurrentUser().getId())) {
            lastReceivedLocation = location;
        }
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

        // Exits
        gameView.scheduleAction(() -> gameView.setExits(new ArrayList<>()));
        for (Long exitId : room.getExits()) {
            GetExitResponseDTO getExitResultDTO = connectionController.sendReceive(new GetExitDTO(room.getId(), exitId), GetExitResponseDTO.class);
            if ( getExitResultDTO != null && getExitResultDTO.getExit() != null ) {
                ExitDto exit = getExitResultDTO.getExit();
                gameView.scheduleAction(() -> gameView.addExit(exit));
            } else {
                System.err.println(getClass().getSimpleName() +".setRoom: exit"+ exitId+" not found!" );
            }
        }
    }

    private void setRoom(RoomDto room) {
        gameView.scheduleAction(() -> gameView.resetView());
        resetRequests();

        updateRoom(room);

        if (currentUser != null ) {
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
        gameView.scheduleAction( () -> gameView.setPlayerLocation(storimWindow.getCurrentUser().getId(), newLocation.getX(), newLocation.getY()));
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
            connectionController.send(requestDTO);
        }
    }


    private long getNextSequenceNumber() {
        return sequenceNumber++;
    }


    @Override
    public void connected() {
        gameView.scheduleAction( () ->  gameView.resetView());
    }

    @Override
    public void disconnected() {
        gameView.scheduleAction(() -> gameView.resetView());
        lastReceivedLocation = null;
        sequenceNumber = 0;
        currentUserAvatar = null;
        currentUser = null;
        resetRequests();
        gameView.scheduleAction(() -> gameView.removePlayer(storimWindow.getCurrentUser().getId()));
    }


//    @Override
//    public void userUpdated(UserDto user) {
//        if (user.getCurrentAvatarID() != null) {
//            AvatarDto avatar = controllers.getUserController().getAvatar(user.getCurrentAvatarID());
//            gameView.scheduleAction(() -> gameView.updatePlayer(user, avatar));
//        } else {
//            gameView.scheduleAction(() -> gameView.updatePlayer(user, null));
//        }
//    }



//    @Override
//    public void thingPlacedInRoom(ThingDto thing, UserDto byUser) {
//        gameView.scheduleAction(() -> gameView.addThing(thing));
//    }
//
//    @Override
//    public void thingRemovedFromRoom(ThingDto thing) {
//        gameView.scheduleAction(() -> gameView.deleteThing(thing.getId()));
//    }
//
//    @Override
//    public void thingInRoom(ThingDto thing) {
//        gameView.scheduleAction(() -> gameView.addThing(thing));
//    }


//
//    @Override
//    public void thingUpdated(ThingDto thing) {
//        if ( thing.getLocation() != null ) {
//            if ( thing.getLocation().getRoomId().equals(gameView.getRoom().getId())) {
//                gameView.scheduleAction(() -> gameView.updateThing(thing));
//            }
//        }
//    }

    public void updateThingLocationRequest(Long thingId, int x, int y) {
        UpdateThingLocationDto updateThingLocationDto = new UpdateThingLocationDto(thingId, x, y);
        connectionController.send(updateThingLocationDto);
    }

    public void setSelectedThing(Long id) {
//        ThingDto thing = controllers.getThingController().findThingById(id);
//        for (ViewListener l : viewListeners ) {
//            l.thingSelectedInView(thing);
//        }
    }

    public void setSelectedExit(Long id) {
//        ExitDto exit = controllers.getRoomController().getExit(id);
//        for (ViewListener l : viewListeners ) {
//            l.exitSelectedInView(exit);
//        }
    }

    public void setSelectedPlayer(Long id) {
//        UserDto user = controllers.getUserController().findUserById(id);
//        for (ViewListener l : viewListeners ) {
//            l.userSelectedInView(user);
//        }
    }

    public void exitClicked(Long id, String name) {
        int result = JOptionPane.showConfirmDialog(storimWindow,"Use exit '"+name+"'?","Use exit",JOptionPane.OK_CANCEL_OPTION);
        if ( result == JOptionPane.OK_OPTION ) {
            connectionController.send(new UseExitRequestDTO(id));
        }
    }
}
