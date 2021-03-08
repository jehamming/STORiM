package nl.hamming.storimapp.controllers;

import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.model.dto.MovementDto;
import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.UserLocationDto;

import java.util.ArrayList;
import java.util.List;

import nl.hamming.storimapp.CalcTools;
import nl.hamming.storimapp.Controllers;
import nl.hamming.storimapp.interfaces.ConnectionListener;
import nl.hamming.storimapp.interfaces.MovementListener;
import nl.hamming.storimapp.interfaces.UserListener;
import nl.hamming.storimapp.view.GameView;

public class ViewController implements MovementListener, ConnectionListener, UserListener {

    private ProtocolHandler protocolHandler;
    private ConnectionController connectionController;
    private UserController userController;
    private MoveController moveController;
    private MovementSender movementSender;
    private GameView gameView;
    private long sequenceNumber;
    private List<MovementDto> movementRequests;
    private UserLocationDto lastreceivedLocation;

    public ViewController(GameView gameView, Controllers controllers) {
        this.connectionController = controllers.getConnectionController();
        this.userController = controllers.getUserController();
        this.moveController = controllers.getMoveController();
        this.gameView = gameView;
        protocolHandler = new ProtocolHandler();
        moveController.addMovementListener(this);
        connectionController.addConnectionListener(this);
        userController.addUserListener(this);
        movementSender = new MovementSender(this, connectionController);
        sequenceNumber = 0;
        movementRequests = new ArrayList<MovementDto>();
    }

    @Override
    public void userMoved(UserDto user, UserLocationDto l) {
        if (user.equals(userController.getCurrentUser())) {
            lastreceivedLocation = l;
            moveCurrentUser(l);
        } else {
            if ( gameView.getPlayer(user.getId()) == null) {
                gameView.addPlayer(user.getId(), user.getName());
            }
           //  Vector3f viewerLocation = getViewerLocation(l);
            gameView.setLocation(user.getId(), l.getX(), l.getY());
        }
    }


    // The method uses client side prediction to counter lag.
    private void moveCurrentUser(UserLocationDto l) {
        gameView.followPlayer(l.getUserId());
        // First : Set the location based on the server respons (server = authoritive
        gameView.setLocation(l.getUserId(), l.getX(), l.getY());
        // Remove all the request before this sequence (if any)
        deleteRequestsUpTO(l.getSequence());
        // Apply all the requests that server has not processed yet.
        applyMoveRequests(l);
        String title = generateTitle(l);
        //TODO  gameView.setTitle(title);
    }

    private String generateTitle(UserLocationDto l) {
        String title = "Room:"+l.getRoomId() + "-X:"+Math.round(l.getX())+",Y:"+Math.round(l.getY());
        return  title;
    }


    private void deleteRequestsUpTO(long sequence) {
        synchronized (movementRequests) {
            List<MovementDto> removeCollection = new ArrayList<MovementDto>();
            for (MovementDto request: movementRequests ) {
                if ( request.getSequence() <= sequence ) {
                    removeCollection.add(request);
                }

            }
            movementRequests.removeAll(removeCollection);
        }
    }

    public void applyMoveRequests(UserLocationDto loc) {
        UserLocationDto locationToCalculateOn = loc;
        synchronized (movementRequests) {
            for (MovementDto dto : movementRequests) {
                locationToCalculateOn = applyMoveRequest(dto, locationToCalculateOn);
            }
        }
    }

    public UserLocationDto applyMoveRequest(MovementDto dto, UserLocationDto loc) {
        UserLocationDto newLocation = CalcTools.calculateNewPosition(dto, loc);
        gameView.setLocation(newLocation.getUserId(), newLocation.getX(), newLocation.getY());
        System.out.println(this.getClass().getName() + "-Scheduled-"+dto.getSequence()+"-"+newLocation.getX()+","+newLocation.getY()+",");
        return newLocation;
    }

    @Override
    public void teleported(UserLocationDto location) {
        UserDto currentUser = userController.getCurrentUser();
        UserLocationDto currentUserLocation = userController.getUserLocation(currentUser.getId());
        UserDto user = userController.getUser(location.getUserId());
        if ( location.getUserId().equals(currentUser)) {
            gameView.resetView();
        }
        if ( location.getRoomId().equals(currentUserLocation.getRoomId())) {
            userMoved(user, location);
        } else {
            // A user teleported to another city
            // RemovePlayer (if present)
            gameView.removePlayer(user.getId());
        }
    }

    public MovementDto getCurrentMoveRequest() {
        MovementDto dto = null;
        //TODO Movement
        boolean forward = false;
        boolean back = false;
        boolean left = false;
        boolean right = false;;
        if (forward || back || left || right) {
            dto = new MovementDto(getNextSequenceNumber(), forward, back, left, right);
            synchronized (movementRequests) {
                movementRequests.add(dto);
            }
            applyMoveRequest(dto, lastreceivedLocation);
        }
        return  dto;
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
        lastreceivedLocation = null;
        sequenceNumber = 0;
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
    public void loginResult(boolean success, String message) {
        if (success) {
            UserDto user = userController.getCurrentUser();
            gameView.addPlayer(user.getId(), user.getName());
        }
    }

}
