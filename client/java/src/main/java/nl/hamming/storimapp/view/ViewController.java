package nl.hamming.storimapp.view;

import com.hamming.storim.CalcTools;
import com.hamming.storim.Controllers;
import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.interfaces.*;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.protocol.MovementRequestDTO;
import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.LocationDto;

import java.util.ArrayList;
import java.util.List;

public class ViewController implements  MovementListener, ConnectionListener, UserListener, RoomListener {

    private ProtocolHandler protocolHandler;
    private Controllers controllers;
    private GameView gameView;
    private long sequenceNumber;
    private List<MovementRequestDTO> movementRequests;
    private LocationDto lastrecievedLocation;
    private Long currentUserid;

    public ViewController(GameView gameView, Controllers controllers) {
        this.controllers = controllers;
        this.gameView = gameView;
        protocolHandler = new ProtocolHandler();
        controllers.getMoveController().addMovementListener(this);
        controllers.getConnectionController().addConnectionListener(this);
        controllers.getUserController().addUserListener(this);
        controllers.getRoomController().addRoomListener(this);
        sequenceNumber = 0;
        movementRequests = new ArrayList<MovementRequestDTO>();
    }


    @Override
    public void userMoved(UserDto user, LocationDto l) {
        if ( controllers.getUserController().getCurrentUserLocation().getRoomId().equals( l.getRoomId())) {
            if (gameView.getPlayer(user.getId()) == null) {
                gameView.addPlayer(user.getId(), user.getName());
            }
            gameView.setLocation(user.getId(), l.getX(), l.getY());
        }
    }

    @Override
    public void currentUserMoved(Long sequence, UserDto user, LocationDto l) {
        lastrecievedLocation = l;
        moveCurrentUser(sequenceNumber,l);
    }

    @Override
    public void teleported(UserDto user, LocationDto location, Long fromRoom) {
        UserDto currentUser = controllers.getUserController().getCurrentUser();
        LocationDto currentUserLocation = controllers.getUserController().getUserLocation(currentUser.getId());
        RoomDto room = controllers.getRoomController().findRoomByID(currentUserLocation.getRoomId());
        if ( user.getId().equals(currentUser.getId())) {
            gameView.resetView();
            gameView.setRoom(room);
            gameView.addPlayer(user.getId(), user.getName());
            gameView.setLocation(user.getId(), location.getX(), location.getY());
            resetRequests();
            lastrecievedLocation = location;
        } else {
            if ( location.getRoomId().equals(currentUserLocation.getRoomId())) {
                gameView.addPlayer(user.getId(), user.getName());
                gameView.setLocation(user.getId(), location.getX(), location.getY());
            } else {
                // A user teleported to another room
                // RemovePlayer (if present)
                gameView.removePlayer(user.getId());
            }
        }
    }


    // The method uses client side prediction to counter lag.
    private void moveCurrentUser(Long sequenceNumber, LocationDto l) {
        UserDto user = controllers.getUserController().getCurrentUser();
        // First : Set the location based on the server respons (server = authoritive
        gameView.setLocation(user.getId(), l.getX(), l.getY());
        // Remove all the request before this sequence (if any)
        deleteRequestsUpTO(sequenceNumber);
        // Apply all the requests that server has not processed yet.
        applyMoveRequests(l);
        String title = generateTitle(l);
        //TODO  gameView.setTitle(title);
    }

    private String generateTitle(LocationDto l) {
        String title = "Room:"+l.getRoomId() + "-X:"+Math.round(l.getX())+",Y:"+Math.round(l.getY());
        return  title;
    }

    private void resetRequests() {
        synchronized (movementRequests) {
            movementRequests =  movementRequests = new ArrayList<MovementRequestDTO>();
        }
    }

    private void deleteRequestsUpTO(long sequence) {
        synchronized (movementRequests) {
            List<MovementRequestDTO> removeCollection = new ArrayList<MovementRequestDTO>();
            for (MovementRequestDTO request: movementRequests ) {
                if ( request.getSequence() <= sequence ) {
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
        gameView.setLocation(controllers.getUserController().getCurrentUser().getId(), newLocation.getX(), newLocation.getY());
        System.out.println(this.getClass().getName() + "-Scheduled-"+dto.getSequence()+"-"+newLocation.getX()+","+newLocation.getY()+",");
        return newLocation;
    }


    private MovementRequestDTO getCurrentMoveRequest(boolean forward, boolean back, boolean left, boolean right) {
        MovementRequestDTO dto = null;
        if (forward || back || left || right) {
            dto = new MovementRequestDTO(getNextSequenceNumber(), forward, back, left, right);
            synchronized (movementRequests) {
                movementRequests.add(dto);
            }
            applyMoveRequest(dto, lastrecievedLocation);
        }
        return  dto;
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
        gameView.resetView();
    }

    @Override
    public void disconnected() {
        gameView.resetView();
        lastrecievedLocation = null;
        sequenceNumber = 0;
        movementRequests = new ArrayList<MovementRequestDTO>();
        gameView.removePlayer(currentUserid);
    }

    @Override
    public void userConnected(UserDto user) {
        // No actions in here
    }

    @Override
    public void userDisconnected(UserDto user) {
        gameView.removePlayer(user.getId());
    }

    @Override
    public void userOnline(UserDto user) {

    }

    @Override
    public void loginResult(boolean success, String message) {
        if (success) {
            UserDto user = controllers.getUserController().getCurrentUser();
            LocationDto userLocation = controllers.getUserController().getUserLocation(user.getId());
            RoomDto room = controllers.getRoomController().findRoomByID(userLocation.getRoomId());
            gameView.setRoom(room);
            gameView.addPlayer(user.getId(), user.getName());
            currentUserid = user.getId();
            currentUserMoved(0L, user, userLocation);
        }
    }

    @Override
    public void currentUserLocation(LocationDto loc) {
        RoomDto room = controllers.getRoomController().findRoomByID(loc.getRoomId());
        gameView.setRoom(room);
    }

    @Override
    public void userLocationUpdate(Long userId, LocationDto loc) {

    }


    @Override
    public void userInRoom(UserDto user, RoomDto room, LocationDto location) {
        if (! user.getId().equals( controllers.getUserController().getCurrentUser().getId())) {
            if (controllers.getUserController().getCurrentUserLocation().getRoomId().equals(room.getId())) {
                gameView.addPlayer(user.getId(), user.getName());
                if (location != null) {
                    gameView.setLocation(user.getId(), location.getX(), location.getY());
                }
            }
        }

    }

    @Override
    public void userTeleportedInRoom(UserDto user, RoomDto room) {
        if ( controllers.getUserController().getCurrentUserLocation().getRoomId().equals( room.getId())) {
            gameView.addPlayer(user.getId(), user.getName());
        }
    }

    @Override
    public void userLeftRoom(UserDto user, RoomDto room) {
        if ( controllers.getUserController().getCurrentUserLocation().getRoomId().equals( room.getId())) {
            gameView.removePlayer(user.getId());
        }
    }

    @Override
    public void roomAdded(RoomDto room) {

    }

    @Override
    public void roomDeleted(RoomDto room) {

    }
}
