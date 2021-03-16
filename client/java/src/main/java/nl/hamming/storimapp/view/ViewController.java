package nl.hamming.storimapp.view;

import com.hamming.storim.CalcTools;
import com.hamming.storim.Controllers;
import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.interfaces.ConnectionListener;
import com.hamming.storim.interfaces.RoomListener;
import com.hamming.storim.interfaces.RoomUpdateListener;
import com.hamming.storim.interfaces.UserListener;
import com.hamming.storim.model.dto.*;
import com.hamming.storim.model.dto.protocol.MovementRequestDTO;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewController implements ConnectionListener, UserListener, RoomListener, RoomUpdateListener {

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
        controllers.getRoomController().addRoomUpdateListener(this);
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
        gameView.setLocation(controllers.getUserController().getCurrentUser().getId(), newLocation.getX(), newLocation.getY());
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
            applyMoveRequest(dto, lastrecievedLocation);
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
    public void userUpdated(UserDto user) {
        AvatarDto avatar = null;
        if (user.getCurrentAvatarID() != null) {
            avatar = controllers.getUserController().getAvatar(user.getCurrentAvatarID());
        }
        gameView.updateUser(user, avatar);
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
            UserDto currentUser = controllers.getUserController().getCurrentUser();
            LocationDto location = controllers.getUserController().getUserLocation(currentUser.getId());
            lastrecievedLocation = location;
            RoomDto room = controllers.getRoomController().findRoomByID(location.getRoomId());
            Image image = null;
            if (currentUser.getCurrentAvatarID() != null) {
                image = controllers.getUserController().getAvatar(currentUser.getCurrentAvatarID()).getImage();
            }
            gameView.addPlayer(currentUser.getId(), currentUser.getName(), image);
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
            gameView.deleteAvatar(user.getId());
        }
    }

    @Override
    public void avatarUpdated(AvatarDto avatar) {
        UserDto user = controllers.getUserController().findUserById(avatar.getOwnerID());
        if (user.getCurrentAvatarID()!= null && user.getCurrentAvatarID().equals(avatar.getId())) {
            avatar = controllers.getUserController().getAvatar(user.getCurrentAvatarID());
            gameView.updateUser(user, avatar);
        }
    }


    @Override
    public void userInRoom(UserDto user, LocationDto location) {
        Image image = null;
        if (user.getCurrentAvatarID() != null) {
            image = controllers.getUserController().getAvatar(user.getCurrentAvatarID()).getImage();
        }
        gameView.addPlayer(user.getId(), user.getName(), image);
        gameView.setLocation(user.getId(), location.getX(), location.getY());
    }

    @Override
    public void userEnteredRoom(UserDto user, LocationDto location) {
        Image image = null;
        if (user.getCurrentAvatarID() != null) {
            image = controllers.getUserController().getAvatar(user.getCurrentAvatarID()).getImage();
        }
        gameView.addPlayer(user.getId(), user.getName(), image);
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
        if (room.getTileID() != null) {
            TileDto tile = controllers.getRoomController().getTile(room.getTileID());
            if (tile != null) {
                gameView.setTile(tile);
            }
        }
        gameView.setRoom(room);
        UserDto currentUser = controllers.getUserController().getCurrentUser();
        Image image = null;
        AvatarDto avatar = controllers.getUserController().getAvatar(currentUser.getCurrentAvatarID());
        if (avatar != null) {
            image = avatar.getImage();
        }
        gameView.addPlayer(currentUser.getId(), currentUser.getName(), image);
        gameView.setLocation(controllers.getUserController().getCurrentUser().getId(), location.getX(), location.getY());
        resetRequests();
        lastrecievedLocation = location;
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
            gameView.setRoom(room);
        }
    }
}
