package com.hamming.storim.server;


import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.DTO;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.dto.protocol.serverpush.*;
import com.hamming.storim.common.dto.protocol.serverpush.old.*;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.GetVerbDetailsResponseDTO;
import com.hamming.storim.server.common.model.*;

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



    public LocationDto getLocationDTO(Location loc) {
        return new LocationDto(loc.getRoom().getId(), loc.getX(), loc.getY());
    }

    public ExitDto getExitDTO(Exit e) {
        return new ExitDto(e.getId(), e.getName(), e.getRoomid(), ExitDto.Orientation.valueOf(e.getOrientation().name()));
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


