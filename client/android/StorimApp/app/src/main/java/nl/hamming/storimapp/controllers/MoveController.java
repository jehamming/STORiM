package nl.hamming.storimapp.controllers;

import nl.hamming.storimapp.Controllers;
import nl.hamming.storimapp.interfaces.MovementListener;
import com.hamming.storim.game.Protocol;
import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.model.dto.*;
import com.hamming.storim.net.CommandReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoveController implements CommandReceiver {

    private ProtocolHandler protocolHandler;
    private ConnectionController connectionController;
    private List<MovementListener> movementListeners;
    private UserController userController;


    public MoveController(Controllers controllers) {
        this.connectionController = controllers.getConnectionController();
        this.userController = controllers.getUserController();
        protocolHandler = new ProtocolHandler();
        movementListeners = new ArrayList<MovementListener>();
        connectionController.registerReceiver(Protocol.Command.TELEPORT, this);
        connectionController.registerReceiver(Protocol.Command.USERTELEPORTED, this);
        connectionController.registerReceiver(Protocol.Command.LOCATION, this);
        connectionController.registerReceiver(Protocol.Command.MOVE, this);
    }

    public void addMovementListener(MovementListener l) {
        movementListeners.add(l);
    }

    public String teleportRequest(UserDto user, RoomDto room) {
        String message = null;
        if (user != null && room != null) {
            String cmd = protocolHandler.getTeleportCommand(user.getId(), room);
            connectionController.send(cmd);
        } else {
            message = "Please select a World, Continent, City and Baseplate to teleport to!";
        }
        return message;
    }


    @Override
    public void receiveCommand(Protocol.Command cmd, String[] data) {
        switch (cmd) {
            case TELEPORT:
                teleportResult(data);
                break;
            case USERTELEPORTED:
                userTeleported(data);
                break;
            case LOCATION:
                handleLocation(data);
                break;
        }
    }

    private void handleLocation(String[] data) {
        UserLocationDto loc = new UserLocationDto();
        loc.setValues(data);
        userController.setUserLocation(loc);
        if (userController.getCurrentUser().getId().equals( loc.getUserId())) {
            // Current logged in user!
            move(userController.getCurrentUser(), loc);
        } else  {
            // Movement of other user!
            UserDto user = userController.getUser(loc.getUserId());
            if (user != null ) {
                move(user, loc);
            }
        }
    }

    private void teleportResult(String[] data) {
        String status = data[0];
        String[] values = Arrays.copyOfRange(data, 1, data.length);
        String msg = "";
        if (Protocol.SUCCESS.equals(status)) {
            UserLocationDto loc = new UserLocationDto();
            loc.setValues(values);
            userController.setUserLocation(loc);
            teleported(loc);
        } else {
            msg = Arrays.toString(values);
            System.out.println("Teleport failed: " + msg);
        }
    }

    private void userTeleported(String[] data) {
        UserLocationDto loc = new UserLocationDto();
        loc.setValues(data);
        userController.setUserLocation(loc);
        teleported(loc);
    }


    private void teleported(UserLocationDto location) {
        for (MovementListener l: movementListeners) {
            l.teleported(location);
        }
    }

    private void move(UserDto user, UserLocationDto location) {
        for (MovementListener l: movementListeners) {
            l.userMoved(user,location);
        }
    }
}
