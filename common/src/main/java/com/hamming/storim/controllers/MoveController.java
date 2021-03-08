package com.hamming.storim.controllers;

import com.hamming.storim.Controllers;
import com.hamming.storim.interfaces.MovementListener;
import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.model.dto.*;
import com.hamming.storim.model.dto.protocol.*;
import com.hamming.storim.net.NetCommandReceiver;

import java.util.ArrayList;
import java.util.List;

public class MoveController {

    private ProtocolHandler protocolHandler;
    private ConnectionController connectionController;
    private List<MovementListener> movementListeners;
    private Controllers controllers;


    public MoveController(Controllers controllers) {
        this.connectionController = controllers.getConnectionController();
        this.controllers = controllers;
        protocolHandler = new ProtocolHandler();
        movementListeners = new ArrayList<MovementListener>();
        connectionController.registerReceiver(TeleportResultDTO.class, new NetCommandReceiver<TeleportResultDTO>() {
            @Override
            public void receiveDTO(TeleportResultDTO dto) {
                teleportResult(dto);
            }
        });
        connectionController.registerReceiver(MovementResultDTO.class, new NetCommandReceiver<MovementResultDTO>() {
            @Override
            public void receiveDTO(MovementResultDTO dto) {
                handleMovementResult(dto);
            }
        });
        connectionController.registerReceiver(UserLocationUpdateDTO.class, new NetCommandReceiver<UserLocationUpdateDTO>() {
            @Override
            public void receiveDTO(UserLocationUpdateDTO dto) {
                handleUserLocationUpdateDTO(dto);
            }
        });
        connectionController.registerReceiver(UserTeleportedDTO.class, new NetCommandReceiver<UserTeleportedDTO>() {
            @Override
            public void receiveDTO(UserTeleportedDTO dto) {
                handleUserTeleportedDTO(dto);
            }
        });
        connectionController.registerReceiver(UserConnectedDTO.class, new NetCommandReceiver<UserConnectedDTO>() {
            @Override
            public void receiveDTO(UserConnectedDTO dto) {
                handleUserConnectedDTO(dto);
            }
        });
    }

    private void handleUserConnectedDTO(UserConnectedDTO dto) {
    }

    private void handleUserTeleportedDTO(UserTeleportedDTO dto) {
        UserDto user = controllers.getUserController().findUserByID(dto.getUserId());
        teleported(user, dto.getLocation(), dto.getFromRoomId());
    }

    private void handleUserLocationUpdateDTO(UserLocationUpdateDTO dto) {
        // Movement of other user!
        UserDto user = controllers.getUserController().findUserByID(dto.getUserId());
        if (user != null ) {
            move(user, dto.getLocation());
        }
    }

    public void addMovementListener(MovementListener l) {
        movementListeners.add(l);
    }

    public String teleport(UserDto user, RoomDto room) {
        String message = null;
        if (user != null && room != null) {
            TeleportRequestDTO dto = protocolHandler.getTeleportRequestDTO(user.getId(), room.getId());
            connectionController.send(dto);
        } else {
            message = "teleport nogo";
        }
        return message;
    }


    private void handleMovementResult(MovementResultDTO dto) {
        LocationDto loc = dto.getLocation();
        Long currentUserID = controllers.getUserController().getCurrentUser().getId();
        controllers.getUserController().setUserLocation(currentUserID, dto.getLocation());
        move(controllers.getUserController().getCurrentUser(), loc);
    }

    private void teleportResult(TeleportResultDTO dto) {
        if (dto.isTeleportSuccesful()) {
            UserDto currentUser = controllers.getUserController().getCurrentUser();
            Long currentUserID = currentUser.getId();
            LocationDto loc = dto.getLocation();
            controllers.getUserController().setUserLocation(currentUserID, dto.getLocation());
            teleported(currentUser, loc, dto.getFromRoomID());
        } else {
            System.out.println("Teleport failed: " + dto.getErrorMessage());
        }
    }


    private void teleported(UserDto user, LocationDto location, Long fromRoomID) {
        for (MovementListener l: movementListeners) {
            l.teleported(user, location, fromRoomID);
        }
    }

    private void move(UserDto user, LocationDto location) {
        for (MovementListener l: movementListeners) {
            l.userMoved(user,location);
        }
    }
}
