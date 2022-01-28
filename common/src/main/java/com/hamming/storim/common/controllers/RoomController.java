package com.hamming.storim.common.controllers;

import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.interfaces.RoomListener;
import com.hamming.storim.common.interfaces.RoomUpdateListener;

import java.util.List;
import java.util.Map;

public class RoomController {

    private List<RoomListener> roomListeners;
    private List<RoomUpdateListener> roomUpdateListeners;
    private Map<Long, RoomDto> rooms;
    private Map<Long, ExitDto> exits;
    private Map<Long, TileDto> tiles;


    public RoomController() {
        registerReceivers();
    }

    private void registerReceivers() {
    }

//    private void connectResult(ConnectResultDTO dto) {
//        if (dto.isConnectSucceeded()) {
//            Long roomId = dto.getLocation().getRoomId();
//            RoomDto room=  getRoom(roomId);
//            for (RoomListener l : roomListeners) {
//                l.setRoom(room, dto.getLocation());
//            }
//        }
//    }
//
//    private void handleGetExitResultDTO(GetExitResultDTO dto) {
//        ExitDto exit = dto.getExit();
//        if ( exit != null ) {
//            exits.put(exit.getId(), exit);
//        }
//
//    }
//
//    private void handleThingInRoomDTO(ThingInRoomDTO dto) {
//        if ( currentUserLocation( dto.getRoom().getId())) {
//            for (RoomListener l : roomListeners) {
//                l.thingInRoom(dto.getThing());
//            }
//        }
//    }
//
//    private void handleThingDeletedDTO(ThingDeletedDTO dto) {
//        if ( currentUserLocation( dto.getThing().getLocation().getRoomId())) {
//            for (RoomListener l : roomListeners) {
//                l.thingRemovedFromRoom(dto.getThing());
//            }
//        }
//    }
//
//    private void handleThingPlacedDTO(ThingPlacedDTO dto) {
//        UserDto user = controllers.getUserController().findUserById(dto.getUserId());
//        if ( currentUserLocation(dto.getThing().getLocation().getRoomId())) {
//            for (RoomListener l : roomListeners) {
//                l.thingPlacedInRoom(dto.getThing(), user);
//            }
//        }
//    }
//
//
//    private void handleRoomAddedDTO(RoomAddedDTO dto) {
//        rooms.put(dto.getRoom().getId(), dto.getRoom());
//        for (RoomUpdateListener l : roomUpdateListeners) {
//            l.roomAdded(dto.getRoom());
//        }
//    }
//
//    private void initVariables() {
//        rooms = new HashMap<>();
//        tiles = new HashMap<>();
//        exits = new HashMap<>();
//        roomListeners = new ArrayList<>();
//        roomUpdateListeners = new ArrayList<>();
//    }
//
//
//    private void handleGetTileResultDTO(GetTileResultDTO dto) {
//        if (dto.isSuccess()) {
//            tiles.put(dto.getTile().getId(), dto.getTile());
//        }
//    }
//
//
//    public boolean currentUserLocation(Long roomID) {
//        return controllers.getUserController().getCurrentUserLocation().getRoomId().equals(roomID);
//    }
//
//    private void handleRoomUpdatedDTO(RoomUpdatedDTO dto) {
//        RoomDto room = dto.getRoom();
//        if (room != null) {
//            rooms.put(room.getId(), room);
//            for (RoomUpdateListener l : roomUpdateListeners) {
//                l.roomUpdated(room);
//            }
//        }
//    }
//
//    private void handleRoomDeletedDTO(RoomDeletedDTO dto) {
//        RoomDto room = getRoom(dto.getRoomId());
//        if (room != null) {
//            rooms.remove(room.getId());
//            for (RoomUpdateListener l : roomUpdateListeners) {
//                l.roomDeleted(room);
//            }
//        }
//    }
//
//
//    private void handleGetRoomResult(GetRoomResultDTO dto) {
//        if (dto.isSuccess()) {
//            rooms.put(dto.getRoom().getId(), dto.getRoom());
//        }
//    }
//
//    public void addRoomUpdateListener(RoomUpdateListener l) {
//        roomUpdateListeners.add(l);
//    }
//
//    public void addRoomListener(RoomListener l) {
//        roomListeners.add(l);
//    }
//
//    public RoomDto getRoom(Long id) {
//        return rooms.get(id);
//    }
//
//    public ExitDto getExit(Long id) {
//        return exits.get(id);
//    }
//
//    public List<RoomDto> getRoomsForUser(Long userId) {
//        List<RoomDto> roomList = new ArrayList<>();
//        for (RoomDto room : rooms.values()) {
//            if (room.getOwnerID().equals(userId)) {
//                roomList.add(room);
//            }
//        }
//        return roomList;
//    }
//
//    public void addRoom(String roomName, Long tileID, byte[] image) {
//        AddRoomDto addRoomDto = ProtocolHandler.getInstance().getAddRoomDTO(roomName, tileID, image);
//        controllers.getConnectionController().send(addRoomDto);
//    }
//
//    public void updateRoom(Long roomId, String roomName, int width, int length, int rows, int cols, Long tileID, byte[] tileImage) {
//        UpdateRoomDto updateRoomDto = ProtocolHandler.getInstance().getUpdateRoomDto(roomId, roomName, width, length, rows, cols, tileID, tileImage);
//        controllers.getConnectionController().send(updateRoomDto);
//    }
//
//    public void deleteRoom(Long roomId) {
//        DeleteRoomDTO deleteRoomDTO = ProtocolHandler.getInstance().getDeleteRoomDto(roomId);
//        controllers.getConnectionController().send(deleteRoomDTO);
//    }
//
//    public void teleportRequest(Long userId, Long roomId) {
//        TeleportRequestDTO dto = ProtocolHandler.getInstance().getTeleportRequestDTO(userId,roomId);
//        controllers.getConnectionController().send(dto);
//    }
//
//    public TileDto getTile(Long tileID) {
//        return tiles.get(tileID);
//    }
//
//    public Collection<TileDto> getTiles() {
//        return tiles.values();
//    }

}
