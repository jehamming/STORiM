package com.hamming.storim.common.controllers;

import com.hamming.storim.common.Controllers;
import com.hamming.storim.common.ProtocolHandler;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.*;
import com.hamming.storim.common.dto.protocol.room.*;
import com.hamming.storim.common.dto.protocol.thing.GetThingResultDTO;
import com.hamming.storim.common.dto.protocol.thing.ThingDeletedDTO;
import com.hamming.storim.common.dto.protocol.thing.ThingInRoomDTO;
import com.hamming.storim.common.dto.protocol.thing.ThingPlacedDTO;
import com.hamming.storim.common.interfaces.RoomListener;
import com.hamming.storim.common.interfaces.RoomUpdateListener;
import com.hamming.storim.common.net.NetCommandReceiver;

import java.util.*;

public class RoomController {

    private ProtocolHandler protocolHandler;
    private List<RoomListener> roomListeners;
    private List<RoomUpdateListener> roomUpdateListeners;
    private Controllers controllers;
    private Map<Long, RoomDto> rooms;
    private Map<Long, TileDto> tiles;


    public RoomController(Controllers controllers) {
        this.controllers = controllers;
        initVariables();
        registerReceivers();
    }

    private void registerReceivers() {
        controllers.getConnectionController().registerReceiver(GetRoomResultDTO.class, (NetCommandReceiver<GetRoomResultDTO>) dto -> handleGetRoomResult(dto));
        controllers.getConnectionController().registerReceiver(RoomAddedDTO.class, (NetCommandReceiver<RoomAddedDTO>) dto -> handleRoomAddedDTO(dto));
        controllers.getConnectionController().registerReceiver(RoomUpdatedDTO.class, (NetCommandReceiver<RoomUpdatedDTO>) dto -> handleRoomUpdatedDTO(dto));
        controllers.getConnectionController().registerReceiver(RoomDeletedDTO.class, (NetCommandReceiver<RoomDeletedDTO>) dto -> handleRoomDeletedDTO(dto));
        controllers.getConnectionController().registerReceiver(UserInRoomDTO.class, (NetCommandReceiver<UserInRoomDTO>) dto -> handleUserInRoomDTO(dto));
        controllers.getConnectionController().registerReceiver(UserTeleportedDTO.class, (NetCommandReceiver<UserTeleportedDTO>) dto -> handleUserTeleportedDTO(dto));
        controllers.getConnectionController().registerReceiver(UserConnectedDTO.class, (NetCommandReceiver<UserConnectedDTO>) dto -> handleUserConnectedDTO(dto));
        controllers.getConnectionController().registerReceiver(UserOnlineDTO.class, (NetCommandReceiver<UserOnlineDTO>) dto -> handleUserOnlineDTO(dto));
        controllers.getConnectionController().registerReceiver(TeleportResultDTO.class, (NetCommandReceiver<TeleportResultDTO>) dto -> handleTeleportResultDTO(dto));
        controllers.getConnectionController().registerReceiver(MovementResultDTO.class, (NetCommandReceiver<MovementResultDTO>) dto -> handleMovementResult(dto));
        controllers.getConnectionController().registerReceiver(UserLocationUpdateDTO.class, (NetCommandReceiver<UserLocationUpdateDTO>) dto -> handleUserLocationUpdateDTO(dto));
        controllers.getConnectionController().registerReceiver(GetTileResultDTO.class, (NetCommandReceiver<GetTileResultDTO>) dto -> handleGetTileResultDTO(dto));
        controllers.getConnectionController().registerReceiver(ThingPlacedDTO.class, (NetCommandReceiver<ThingPlacedDTO>) dto -> handleThingPlacedDTO(dto));
        controllers.getConnectionController().registerReceiver(ThingDeletedDTO.class, (NetCommandReceiver<ThingDeletedDTO>) dto -> handleThingDeletedDTO(dto));
        controllers.getConnectionController().registerReceiver(ThingInRoomDTO.class, (NetCommandReceiver<ThingInRoomDTO>) dto -> handleThingInRoomDTO(dto));
    }

    private void handleThingInRoomDTO(ThingInRoomDTO dto) {
        if ( currentUserLocation( dto.getRoom().getId())) {
            for (RoomListener l : roomListeners) {
                l.thingInRoom(dto.getThing());
            }
        }
    }

    private void handleThingDeletedDTO(ThingDeletedDTO dto) {
        if ( currentUserLocation( dto.getThing().getLocation().getRoomId())) {
            for (RoomListener l : roomListeners) {
                l.thingRemovedFromRoom(dto.getThing());
            }
        }
    }

    private void handleThingPlacedDTO(ThingPlacedDTO dto) {
        UserDto user = controllers.getUserController().findUserById(dto.getUserId());
        if ( currentUserLocation(dto.getThing().getLocation().getRoomId())) {
            for (RoomListener l : roomListeners) {
                l.thingPlacedInRoom(dto.getThing(), user);
            }
        }
    }


    private void handleRoomAddedDTO(RoomAddedDTO dto) {
        rooms.put(dto.getRoom().getId(), dto.getRoom());
        for (RoomUpdateListener l : roomUpdateListeners) {
            l.roomAdded(dto.getRoom());
        }
    }

    private void initVariables() {
        protocolHandler = new ProtocolHandler();
        rooms = new HashMap<>();
        tiles = new HashMap<>();
        roomListeners = new ArrayList<>();
        roomUpdateListeners = new ArrayList<>();
    }


    private void handleGetTileResultDTO(GetTileResultDTO dto) {
        if (dto.isSuccess()) {
            tiles.put(dto.getTile().getId(), dto.getTile());
        }
    }


    public boolean currentUserLocation(Long roomID) {
        return controllers.getUserController().getCurrentUserLocation().getRoomId().equals(roomID);
    }

    private void handleUserLocationUpdateDTO(UserLocationUpdateDTO dto) {
        UserDto user = controllers.getUserController().findUserById(dto.getUserId());
        if (user != null && currentUserLocation(dto.getLocation().getRoomId())) {
            for (RoomListener l : roomListeners) {
                l.userLocationUpdate(user, dto.getLocation());
            }
        }
    }

    private void handleMovementResult(MovementResultDTO dto) {
        Long currentUserID = controllers.getUserController().getCurrentUser().getId();
        controllers.getUserController().setUserLocation(currentUserID, dto.getLocation());
        if (currentUserLocation(dto.getLocation().getRoomId())) {
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
        if (currentUserLocation(dto.getLocation().getRoomId())) {
            for (RoomListener l : roomListeners) {
                l.userInRoom(dto.getUser(), dto.getLocation());
            }
        }
    }

    private void handleUserConnectedDTO(UserConnectedDTO dto) {
        UserDto user = controllers.getUserController().findUserById(dto.getUser().getId());
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
        UserDto user = controllers.getUserController().findUserById(dto.getUserId());
        controllers.getUserController().setUserLocation(dto.getUserId(), dto.getLocation());
        if (currentUserLocation(dto.getLocation().getRoomId())) {
            for (RoomListener l : roomListeners) {
                l.userEnteredRoom(user, dto.getLocation());
            }
        } else if (currentUserLocation(dto.getFromRoomId())) {
            for (RoomListener l : roomListeners) {
                l.userLeftRoom(user);
            }
        }
    }

    private void handleUserInRoomDTO(UserInRoomDTO dto) {
        UserDto user = controllers.getUserController().findUserById(dto.getUserId());
        if (currentUserLocation(dto.getLocation().getRoomId())) {
            for (RoomListener l : roomListeners) {
                l.userInRoom(user, dto.getLocation());
            }
        }
    }

    private void handleGetRoomResult(GetRoomResultDTO dto) {
        if (dto.isSuccess()) {
            rooms.put(dto.getRoom().getId(), dto.getRoom());
        }
    }

    public void addRoomUpdateListener(RoomUpdateListener l) {
        roomUpdateListeners.add(l);
    }

    public void addRoomListener(RoomListener l) {
        roomListeners.add(l);
    }

    public RoomDto findRoomByID(Long id) {
        return rooms.get(id);
    }

    public List<RoomDto> getRoomsForUser(Long userId) {
        List<RoomDto> roomList = new ArrayList<>();
        for (RoomDto room : rooms.values()) {
            if (room.getOwnerID().equals(userId)) {
                roomList.add(room);
            }
        }
        return roomList;
    }

    public void addRoom(String roomName, Long tileID, byte[] image) {
        AddRoomDto addRoomDto = protocolHandler.getAddRoomDTO(roomName, tileID, image);
        controllers.getConnectionController().send(addRoomDto);
    }

    public void updateRoom(Long roomId, String roomName, int width, int length, int rows, int cols, Long tileID, byte[] tileImage) {
        UpdateRoomDto updateRoomDto = protocolHandler.getUpdateRoomDto(roomId, roomName, width, length, rows, cols, tileID, tileImage);
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

    public TileDto getTile(Long tileID) {
        return tiles.get(tileID);
    }

    public Collection<TileDto> getTiles() {
        return tiles.values();
    }

}
