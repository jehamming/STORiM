package nl.hamming.storimapp.controllers;

import com.hamming.storim.common.CalcTools;
import com.hamming.storim.common.MicroServerException;
import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.MovementRequestDTO;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarSetDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ExitAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ExitDeletedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ExitInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ExitLocationUpdatedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ExitUpdatedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.LocationUpdateDTO;
import com.hamming.storim.common.dto.protocol.serverpush.MessageInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.RoomUpdatedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ThingInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ThingUpdatedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserDisconnectedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserEnteredRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserLeftRoomDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.common.view.ViewListener;

import java.util.ArrayList;
import java.util.List;

import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.TileSet;

public class GameViewController implements ConnectionListener {

    private GameView gameView;
    private long sequenceNumber;
    private List<MovementRequestDTO> movementRequests;
    private LocationDto lastReceivedLocation;
    private List<ViewListener> viewListeners;
    private UserDto currentUser;

    private RoomDto currentRoom;
    private AvatarDto currentUserAvatar;
    private MicroServerProxy microServerProxy;

    public GameViewController(GameView gameView, MicroServerProxy microServerProxy) {
        this.gameView = gameView;
        this.microServerProxy = microServerProxy;
        microServerProxy.getConnectionController().addConnectionListener(this);
        sequenceNumber = 0;
        movementRequests = new ArrayList<>();
        registerReceivers();
        gameView.setgameViewController(this);
        gameView.start();
    }

    private void registerReceivers() {
        microServerProxy.getConnectionController().registerReceiver(SetRoomDTO.class, (ProtocolReceiver<SetRoomDTO>) dto -> setRoom(dto.getRoom()));
        microServerProxy.getConnectionController().registerReceiver(UserInRoomDTO.class, (ProtocolReceiver<UserInRoomDTO>) dto -> userInRoom(dto));
        microServerProxy.getConnectionController().registerReceiver(LocationUpdateDTO.class, (ProtocolReceiver<LocationUpdateDTO>) dto -> locationUpdate(dto));
        microServerProxy.getConnectionController().registerReceiver(UserDisconnectedDTO.class, (ProtocolReceiver<UserDisconnectedDTO>) dto -> userDisconnected(dto));
        microServerProxy.getConnectionController().registerReceiver(UserLeftRoomDTO.class, (ProtocolReceiver<UserLeftRoomDTO>) dto -> userLeftRoom(dto));
        microServerProxy.getConnectionController().registerReceiver(UserEnteredRoomDTO.class, (ProtocolReceiver<UserEnteredRoomDTO>) dto -> userEnteredRoom(dto));
        microServerProxy.getConnectionController().registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto));
        microServerProxy.getConnectionController().registerReceiver(AvatarSetDTO.class, (ProtocolReceiver<AvatarSetDTO>) dto -> setAvatar(dto));
        microServerProxy.getConnectionController().registerReceiver(RoomUpdatedDTO.class, (ProtocolReceiver<RoomUpdatedDTO>) dto -> roomUpdated(dto));
        microServerProxy.getConnectionController().registerReceiver(ThingInRoomDTO.class, (ProtocolReceiver<ThingInRoomDTO>) dto -> addThing(dto));
        microServerProxy.getConnectionController().registerReceiver(ThingUpdatedDTO.class, (ProtocolReceiver<ThingUpdatedDTO>) dto -> updateThing(dto.getThing()));
        microServerProxy.getConnectionController().registerReceiver(ExitAddedDTO.class, (ProtocolReceiver<ExitAddedDTO>) dto -> addExit(dto.getExitDto()));
        microServerProxy.getConnectionController().registerReceiver(ExitInRoomDTO.class, (ProtocolReceiver<ExitInRoomDTO>) dto -> addExit(dto.getExitDto()));
        microServerProxy.getConnectionController().registerReceiver(ExitDeletedDTO.class, (ProtocolReceiver<ExitDeletedDTO>) dto -> deleteExit(dto.getExitID()));
        microServerProxy.getConnectionController().registerReceiver(ExitUpdatedDTO.class, (ProtocolReceiver<ExitUpdatedDTO>) dto -> updateExit(dto.getExitDto()));
        microServerProxy.getConnectionController().registerReceiver(ExitLocationUpdatedDTO.class, (ProtocolReceiver<ExitLocationUpdatedDTO>) dto -> updateExitLocation(dto.getExitId(), dto.getX(), dto.getY()));
        microServerProxy.getConnectionController().registerReceiver(MessageInRoomDTO.class, (ProtocolReceiver<MessageInRoomDTO>) dto -> messageInRoom(dto));
    }

    private void messageInRoom(MessageInRoomDTO dto) {
        if (dto.getMessageType().equals(MessageInRoomDTO.mType.VERB)) {
            gameView.scheduleAction(() -> {
                gameView.addSpeechBalloon(dto.getSourceID());
            });
        }
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
        //gameView.scheduleAction(() -> gameView.addExit(exitDto, windowController.getCurrentServerId()));
        gameView.scheduleAction(() -> gameView.addExit(exitDto, "TODO-FIXTHIS"));
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
                if (dto.getObjectId().equals(currentUser.getId())) {
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
        }
        lastReceivedLocation = l;
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
        if (currentUser != null && user.getId().equals(currentUser.getId())) {
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
        gameView.scheduleAction(() -> gameView.setCurrentUserId(currentUser.getId()));
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
            gameView.scheduleAction(() -> gameView.addPlayer(user.getId(), user.getName(), null));
        } else {
            gameView.scheduleAction(() -> gameView.addPlayer(user.getId(), user.getName(), null));
        }
        gameView.scheduleAction(() -> gameView.setPlayerLocation(user.getId(), location.getX(), location.getY()));
    }

    private void updateRoom(RoomDto room) {
        gameView.scheduleAction(() -> gameView.setRoom(room));
        setBackground(room);
        setForeground(room);
    }

    private void setBackground(RoomDto room) {
        Long tileSetId = room.getBackTileSetId();
        if ( tileSetId != null ) {
            try {
                TileSetDto tileSetDto = microServerProxy.getTileSet(tileSetId);
                if ( tileSetDto != null ) {
                    TileSet tileSet = new TileSet(tileSetDto);
                    gameView.scheduleAction(() -> gameView.setBackground(tileSet, room.getBackTileMap()));
                } else {
                    System.err.println("Foreground TileSet " + tileSetId +" not found on server");
                }
            } catch (MicroServerException e) {
                System.err.println(e.getMessage());
            }
        }
    }


    private void setForeground(RoomDto room) {
        Long tileSetId = room.getFrontTileSetId();
        if ( tileSetId != null ) {
            try {
                TileSetDto tileSetDto = microServerProxy.getTileSet(tileSetId);
                if ( tileSetDto != null ) {
                    TileSet tileSet = new TileSet(tileSetDto);
                    gameView.scheduleAction(() -> gameView.setForeground(tileSet, room.getFrontTileMap()));
                } else {
                    System.err.println("Foreground TileSet " + tileSetId +" not found on server");
                }
            } catch (MicroServerException e) {
                System.err.println(e.getMessage());
            }
        }
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
        checkBoundaries( newLocation );
        gameView.scheduleAction(() -> gameView.setPlayerLocation(currentUser.getId(), newLocation.getX(), newLocation.getY()));
        Logger.info(this, "ScheduledMove-Sequence:" + dto.getSequence() + "-" + newLocation.getX() + "," + newLocation.getY() + ",");
        return newLocation;
    }

    private void checkBoundaries(LocationDto loc) {
        int maxWidth = (int) (gameView.getUnitX() * gameView.getWidth());
        int maxHeight = (int) (gameView.getUnitY() * gameView.getHeight());
        if ( loc.getX() > maxWidth) loc.setX( maxWidth);
        if ( loc.getX() < 0 ) loc.setX(0);
        if ( loc.getY() > maxHeight) {
            loc.setY(maxHeight);
        }
        if ( loc.getY() < 0 ) {
            loc.setY(0);
        }
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
            microServerProxy.getConnectionController().send(requestDTO);
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
        if ( currentUser != null ) {
            gameView.scheduleAction(() -> gameView.removePlayer(currentUser.getId()));
        }
    }

    public void updateThingLocationRequest(Long thingId, int x, int y) {
        microServerProxy.updateThingLocation(thingId, x, y);
    }

    public void exitClicked(Long id, String name, String roomURI) {
        if ( roomURI == null ) {
                microServerProxy.useExit(id);
        } else {
            //TODO Exit to another server
//            // To another server
//            int result = JOptionPane.showConfirmDialog(windowController.getWindow(), "Use exit '" + roomURI + "' to another server, are you sure?", "Use exit " + roomURI, JOptionPane.OK_CANCEL_OPTION);
//            if (result == JOptionPane.OK_OPTION) {
//                windowController.useExitToOtherServer(roomURI);
//            }
        }
    }

    public void updateExitLocationRequest(Long exitID, int x, int y) {
        microServerProxy.updateExitLocation(exitID,currentRoom.getId(), x, y);
    }

    public void setTitle(String text) {
        //TODO setTitle
    }
}
