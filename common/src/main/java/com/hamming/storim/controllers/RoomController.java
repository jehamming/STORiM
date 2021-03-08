package com.hamming.storim.controllers;

import com.hamming.storim.Controllers;
import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.interfaces.RoomListener;
import com.hamming.storim.interfaces.RoomUpdateListener;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.protocol.*;
import com.hamming.storim.net.NetCommandReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomController  {

    private ProtocolHandler protocolHandler;
    private List<RoomListener> roomListeners;
    private List<RoomUpdateListener> roomUpdateListeners;
    private Controllers controllers;
    private Map<Long, RoomDto> rooms;


    public RoomController(Controllers controllers) {
        this.controllers = controllers;
        protocolHandler = new ProtocolHandler();
        rooms = new HashMap<Long, RoomDto>();
        roomListeners = new ArrayList<RoomListener>();
        roomUpdateListeners = new ArrayList<RoomUpdateListener>();
        controllers.getConnectionController().registerReceiver(GetRoomResultDTO.class, new NetCommandReceiver<GetRoomResultDTO>() {
            @Override
            public void receiveDTO(GetRoomResultDTO dto) {
                handleGetRoomResult(dto);
            }
        });
        controllers.getConnectionController().registerReceiver(RoomUpdatedDTO.class, new NetCommandReceiver<RoomUpdatedDTO>() {
            @Override
            public void receiveDTO(RoomUpdatedDTO dto) {
                handleRoomUpdatedDTO(dto);
            }
        });
        controllers.getConnectionController().registerReceiver(RoomDeletedDTO.class, new NetCommandReceiver<RoomDeletedDTO>() {
            @Override
            public void receiveDTO(RoomDeletedDTO dto) {
                handleRoomDeletedDTO(dto);
            }
        });
        controllers.getConnectionController().registerReceiver(UserInRoomDTO.class, new NetCommandReceiver<UserInRoomDTO>() {
            @Override
            public void receiveDTO(UserInRoomDTO dto) {
                handleUserInRoomDTO(dto);
            }
        });
        controllers.getConnectionController().registerReceiver(UserTeleportedDTO.class, new NetCommandReceiver<UserTeleportedDTO>() {
            @Override
            public void receiveDTO(UserTeleportedDTO dto) {
                handleUserTeleportedDTO(dto);
            }
        });

        controllers.getConnectionController().registerReceiver(UserConnectedDTO.class, new NetCommandReceiver<UserConnectedDTO>() {
            @Override
            public void receiveDTO(UserConnectedDTO dto) {
                handleUserConnectedDTO(dto);
            }
        });
        controllers.getConnectionController().registerReceiver(UserOnlineDTO.class, new NetCommandReceiver<UserOnlineDTO>() {
            @Override
            public void receiveDTO(UserOnlineDTO dto) {
                handleUserOnlineDTO(dto);
            }
        });
        controllers.getConnectionController().registerReceiver(TeleportResultDTO.class, new NetCommandReceiver<TeleportResultDTO>() {
            @Override
            public void receiveDTO(TeleportResultDTO dto) {
                handleTeleportResultDTO(dto);
            }
        });
        controllers.getConnectionController().registerReceiver(MovementResultDTO.class, new NetCommandReceiver<MovementResultDTO>() {
            @Override
            public void receiveDTO(MovementResultDTO dto) {
                handleMovementResult(dto);
            }
        });
        controllers.getConnectionController().registerReceiver(UserLocationUpdateDTO.class, new NetCommandReceiver<UserLocationUpdateDTO>() {
            @Override
            public void receiveDTO(UserLocationUpdateDTO dto) {
                handleUserLocationUpdateDTO(dto);
            }
        });
    }


    public boolean currentUserLocation(Long roomID) {
        return controllers.getUserController().getCurrentUserLocation().getRoomId().equals(roomID);
    }

    private void handleUserLocationUpdateDTO(UserLocationUpdateDTO dto) {
        UserDto user = controllers.getUserController().findUserByID(dto.getUserId());
        if (user != null && currentUserLocation(dto.getLocation().getRoomId())) {
            for (RoomListener l : roomListeners) {
                l.userLocationUpdate(user, dto.getLocation());
            }
        }
    }

    private void handleMovementResult(MovementResultDTO dto) {
        Long currentUserID = controllers.getUserController().getCurrentUser().getId();
        controllers.getUserController().setUserLocation(currentUserID, dto.getLocation());
        if (currentUserLocation( dto.getLocation().getRoomId())) {
            for (RoomListener l : roomListeners) {
                l.currentUserLocationUpdate(dto.getSequence(), dto.getLocation());
            }
        }
    }

    private void handleTeleportResultDTO(TeleportResultDTO dto) {
        RoomDto room = findRoomByID(dto.getLocation().getRoomId());
        controllers.getUserController().setCurrentUserLocation(dto.getLocation());
        for (RoomListener l : roomListeners) {
            l.setRoom(room, dto.getLocation());
        }
    }


    private void handleUserOnlineDTO(UserOnlineDTO dto) {
        RoomDto room = findRoomByID(dto.getLocation().getRoomId());
        if (currentUserLocation(dto.getLocation().getRoomId())) {
            for (RoomListener l : roomListeners) {
                l.userInRoom(dto.getUser(), dto.getLocation());
            }
        }
    }

    private void handleUserConnectedDTO(UserConnectedDTO dto) {
        UserDto user = controllers.getUserController().findUserByID(dto.getUser().getId());
        if (currentUserLocation(dto.getLocation().getRoomId())) {
            for (RoomListener l : roomListeners) {
                l.userInRoom(user, dto.getLocation());
            }
        }
    }

    private void handleRoomUpdatedDTO(RoomUpdatedDTO dto) {
        RoomDto room = dto.getRoom();
        if (room != null) {
            rooms.put(room.getId(), room);
            for (RoomUpdateListener l : roomUpdateListeners) {
                l.roomUpdated(room);
            }
        }
    }

    private void handleRoomDeletedDTO(RoomDeletedDTO dto) {
        RoomDto room = findRoomByID(dto.getRoomId());
        if (room != null) {
            rooms.remove(room.getId());
            for (RoomUpdateListener l : roomUpdateListeners) {
                l.roomDeleted(room);
            }
        }
    }

    private void handleUserTeleportedDTO(UserTeleportedDTO dto) {
        UserDto user = controllers.getUserController().findUserByID(dto.getUserId());
        controllers.getUserController().setUserLocation(dto.getUserId(), dto.getLocation());
        if (currentUserLocation(dto.getLocation().getRoomId())) {
            for (RoomListener l : roomListeners) {
                l.userEnteredRoom(user, dto.getLocation());
            }
        } else if ( currentUserLocation(dto.getFromRoomId())) {
            for (RoomListener l : roomListeners) {
                l.userLeftRoom(user);
            }
        }
    }

    private void handleUserInRoomDTO(UserInRoomDTO dto) {
        UserDto user = controllers.getUserController().findUserByID(dto.getUserId());
        if (currentUserLocation(dto.getLocation().getRoomId())) {
            for (RoomListener l : roomListeners) {
                l.userInRoom(user, dto.getLocation());
            }
        }
    }

    private void handleGetRoomResult(GetRoomResultDTO dto) {
        if (dto.isSuccess()) {
            rooms.put(dto.getRoom().getId(), dto.getRoom());
            for (RoomUpdateListener l : roomUpdateListeners) {
                l.roomAdded(dto.getRoom());
            }
        }
    }

    public void addRoomUpdateListener( RoomUpdateListener l) {
        roomUpdateListeners.add(l);
    }

    public void addRoomListener(RoomListener l) {
        roomListeners.add(l);
    }

    public RoomDto findRoomByID(Long id) {
        return rooms.get(id);
    }

    public List<RoomDto> getRoomsForUser(Long userId) {
        List<RoomDto> roomList = new ArrayList<RoomDto>();
        for (RoomDto room : rooms.values()) {
            if (room.getOwnerID().equals(userId)) {
                roomList.add(room);
            }
        }
        return roomList;
    }

    public void addRoom(String roomName, int roomSize) {
        AddRoomDto addRoomDto = protocolHandler.getAddRoomDTO(roomName, roomSize);
        controllers.getConnectionController().send(addRoomDto);
    }

    public void updateRoom(Long roomId, String roomName, int roomSize) {
        UpdateRoomDto updateRoomDto = protocolHandler.getUpdateRoomDto(roomId, roomName, roomSize);
        controllers.getConnectionController().send(updateRoomDto);
    }

    public void deleteRoom(Long roomId) {
        DeleteRoomDTO deleteRoomDTO = protocolHandler.getDeleteRoomDto(roomId);
        controllers.getConnectionController().send(deleteRoomDTO);
    }

    public void teleportRequest(UserDto currentUser, RoomDto room) {
        TeleportRequestDTO dto = protocolHandler.getTeleportRequestDTO(currentUser.getId(), room.getId());
        controllers.getConnectionController().send(dto);
    }
}
