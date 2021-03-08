package com.hamming.storim.controllers;

import com.hamming.storim.Controllers;
import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.interfaces.MovementListener;
import com.hamming.storim.interfaces.RoomListener;
import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.protocol.*;
import com.hamming.storim.net.NetCommandReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomController implements MovementListener {

    private ProtocolHandler protocolHandler;
    private List<RoomListener> roomListeners;
    private Controllers controllers;
    private Map<Long, RoomDto> rooms;


    public RoomController(Controllers controllers) {
        controllers.getMoveController().addMovementListener(this);
        this.controllers = controllers;
        protocolHandler = new ProtocolHandler();
        rooms = new HashMap<Long, RoomDto>();
        roomListeners = new ArrayList<RoomListener>();
        controllers.getConnectionController().registerReceiver(GetRoomResultDTO.class, new NetCommandReceiver<GetRoomResultDTO>() {
            @Override
            public void receiveDTO(GetRoomResultDTO dto) {
                handleGetRoomResult(dto);
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
        controllers.getConnectionController().registerReceiver(RoomDeletedDTO.class, new NetCommandReceiver<RoomDeletedDTO>() {
            @Override
            public void receiveDTO(RoomDeletedDTO dto) {
                handleRoomDeletedDTO(dto);
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

    }


    private void handleUserOnlineDTO(UserOnlineDTO dto) {
        RoomDto room = findRoomByID(dto.getLocation().getRoomId());
        for (RoomListener l : roomListeners) {
            l.userInRoom(dto.getUser(), room, dto.getLocation());
        }
    }

    private void handleUserConnectedDTO(UserConnectedDTO dto) {
        UserDto user = controllers.getUserController().findUserByID(dto.getUser().getId());
        RoomDto room = findRoomByID(dto.getLocation().getRoomId());
        for (RoomListener l : roomListeners) {
            l.userInRoom(user, room, dto.getLocation());
        }
    }

    private void handleRoomDeletedDTO(RoomDeletedDTO dto) {
        RoomDto room = findRoomByID(dto.getRoomId());
        if (room != null) {
            rooms.remove(room.getId());
            for (RoomListener l : roomListeners) {
                l.roomDeleted(room);
            }
        }
    }

    private void handleUserTeleportedDTO(UserTeleportedDTO dto) {
        UserDto user = controllers.getUserController().findUserByID(dto.getUserId());
        for (RoomListener l : roomListeners) {
            RoomDto room = findRoomByID(dto.getLocation().getRoomId());
            RoomDto fromRoom = findRoomByID(dto.getFromRoomId());
            l.userLeftRoom(user, fromRoom );
         //   l.userTeleportedInRoom(user, room);
        }
    }

    private void handleUserInRoomDTO(UserInRoomDTO dto) {
        UserDto user = controllers.getUserController().findUserByID(dto.getUserId());
        RoomDto room = findRoomByID(dto.getRoomId());
        for (RoomListener l : roomListeners) {
            l.userInRoom(user, room , dto.getLocation());
        }
    }

    private void handleGetRoomResult(GetRoomResultDTO dto) {
        if (dto.isSuccess()) {
            rooms.put(dto.getRoom().getId(), dto.getRoom());
            for (RoomListener l : roomListeners) {
                l.roomAdded(dto.getRoom());
            }
        }
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

    @Override
    public void userMoved(UserDto user, LocationDto newUserLocation) {

    }

    @Override
    public void currentUserMoved(Long sequence, UserDto user, LocationDto newUserLocation) {

    }

    @Override
    public void teleported(UserDto user, LocationDto location, Long fromRoomID) {
        if (! user.getId().equals(controllers.getUserController().getCurrentUser().getId())) {
            RoomDto fromRoom = findRoomByID(fromRoomID);
            RoomDto toRoom = findRoomByID(location.getRoomId());
            for (RoomListener l : roomListeners) {
                l.userLeftRoom(user, fromRoom);
            }
        }
    }
}
