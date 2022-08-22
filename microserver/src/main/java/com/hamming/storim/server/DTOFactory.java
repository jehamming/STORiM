package com.hamming.storim.server;


import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.serverpush.UserEnteredRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserInRoomDTO;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.model.BasicObject;
import com.hamming.storim.server.common.model.Exit;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.common.model.Room;

import java.util.ArrayList;
import java.util.List;

public class DTOFactory {

    private static DTOFactory instance;

    private DTOFactory() {
    }

    public static DTOFactory getInstance() {
        if (instance == null) {
            instance = new DTOFactory();
        }
        return instance;
    }

    private DTO fillBasicObjectInfo(DTO dto, BasicObject basicObject) {
        dto.setCreatorID(basicObject.getCreatorId());
        dto.setName(basicObject.getName());
        dto.setOwnerID(basicObject.getOwnerId());
        return dto;
    }



    public LocationDto getLocationDTO(Location l) {
        return new LocationDto(l.getObjectId(), l.getServerId(), l.getRoomId(), l.getX(), l.getY());
    }

    public ExitDto getExitDTO(Exit e) {
        ExitDto exitDto = new ExitDto(e.getId(), e.getName(), e.getToServerID(), e.getToRoomID(), e.getDescription(), e.getScale(), e.getRotation(), ImageUtils.encode(e.getImage()), e.getX(), e.getY());
        fillBasicObjectInfo(exitDto, e);
        return exitDto;
    }


    public RoomDto getRoomDto(Room b) {
        List<Long> exits = new ArrayList<>();
        for ( Exit e : b.getExits()) {
            exits.add(e.getId());
        }
        RoomDto dto = new RoomDto(b.getId(), b.getName(), b.getWidth(), b.getLength(), b.getRows(), b.getCols(), b.getTileId(), exits);
        fillBasicObjectInfo(dto, b);
        return dto;
    }

    public UserInRoomDTO getUserInRoomDTO(UserDto userDto, Location location) {
        LocationDto locationDto = getLocationDTO(location);
        return new UserInRoomDTO(userDto, locationDto);
    }

    public UserEnteredRoomDTO getUserEnteredRoomDTO(UserDto userDto, Location location, Long oldRoomId, String oldRoomName) {
        LocationDto locationDto = getLocationDTO(location);
        return new UserEnteredRoomDTO(userDto, locationDto, oldRoomId, oldRoomName);
    }

}


