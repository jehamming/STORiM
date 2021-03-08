package nl.hamming.storimapp.view;

import com.hamming.storim.CalcTools;
import com.hamming.storim.Controllers;
import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.interfaces.ConnectionListener;
import com.hamming.storim.interfaces.RoomListener;
import com.hamming.storim.interfaces.UserListener;
import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.protocol.MovementRequestDTO;

import java.util.ArrayList;
import java.util.List;

public class ViewController implements  ConnectionListener, UserListener, RoomListener {

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
        controllers.getConnectionController().addConnectionListener(this);
        controllers.getUserController().addUserListener(this);
        controllers.getRoomController().addRoomListener(this);
        sequenceNumber = 0;
        movementRequests = new ArrayList<MovementRequestDTO>();
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
            LocationDto location = controllers.getUserController().getUserLocation(user.getId());
            lastrecievedLocation = location;
            RoomDto room = controllers.getRoomController().findRoomByID(location.getRoomId());
            gameView.setRoom(room);
            gameView.addPlayer(user.getId(), user.getName());
            gameView.setLocation(user.getId(), location.getX(), location.getY());
            currentUserid = user.getId();
        }
    }

    @Override
    public void userTeleported(Long userId, LocationDto location) {

    }


    @Override
    public void userInRoom(UserDto user, LocationDto location) {
        gameView.addPlayer(user.getId(), user.getName());
        gameView.setLocation(user.getId(), location.getX(), location.getY());
    }

    @Override
    public void userEnteredRoom(UserDto user, LocationDto location) {
        gameView.addPlayer(user.getId(), user.getName());
        gameView.setLocation(user.getId(), location.getX(), location.getY());
    }

    @Override
    public void userLeftRoom(UserDto user) {
        gameView.removePlayer(user.getId());
    }

    @Override
    public void userLocationUpdate(UserDto user, LocationDto location) {
        gameView.setLocation(user.getId(), location.getX(), location.getY());
    }

    @Override
    public void currentUserLocationUpdate(Long sequenceNumber, LocationDto location) {
        moveCurrentUser(sequenceNumber, location);
    }

    @Override
    public void setRoom(RoomDto room, LocationDto location) {
        gameView.resetView();
        gameView.setRoom(room);
        gameView.addPlayer(controllers.getUserController().getCurrentUser().getId(), controllers.getUserController().getCurrentUser().getName());
        gameView.setLocation(controllers.getUserController().getCurrentUser().getId(), location.getX(), location.getY());
        resetRequests();
        lastrecievedLocation = location;
    }
}
