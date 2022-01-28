package com.hamming.storim.server.common.dto;


import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.DTO;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.dto.protocol.serverpush.UserDisconnectedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserEnteredRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.*;
import com.hamming.storim.server.common.ImageUtils;
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

    private com.hamming.storim.common.dto.DTO fillBasicObjectInfo(DTO dto, BasicObject basicObject) {
        dto.setCreatorID(basicObject.getCreatorId());
        dto.setName(basicObject.getName());
        dto.setOwnerID(basicObject.getOwnerId());
        return dto;
    }

    public UserDto getUserDTO(User u) {
        UserDto dto = null;
        if (u.getCurrentAvatar() != null) {
            dto = new UserDto(u.getId(), u.getName(), u.getUsername(), u.getPassword(), u.getEmail(), u.getCurrentAvatar().getId());
        } else {
            dto = new UserDto(u.getId(), u.getName(), u.getUsername(), u.getPassword(), u.getEmail(), null);
        }
        fillBasicObjectInfo(dto, u);
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
        RoomDto dto = new RoomDto(b.getId(), b.getName(), b.getWidth(), b.getLength(), b.getRows(), b.getCols(), null, exits);
        fillBasicObjectInfo(dto, b);
        return dto;
    }

    public VerbDto getVerbDto(Verb c) {
        VerbDto dto = new VerbDto(c.getId(), c.getName());
        fillBasicObjectInfo(dto, c);
        return dto;
    }

    public VerbDetailsDTO getVerbDetailsDto(Verb c) {
        VerbDetailsDTO dto = new VerbDetailsDTO(c.getId(), c.getName(), c.getToCaller(), c.getToLocation());
        fillBasicObjectInfo(dto, c);
        return dto;
    }

    public AvatarDto getAvatarDTO(Avatar avatar) {
        AvatarDto dto = new AvatarDto(avatar.getId(), avatar.getName(), ImageUtils.encode(avatar.getImage()));
        fillBasicObjectInfo(dto, avatar);
        return dto;
    }

    public TileDto getTileDTO(Tile tile) {
        TileDto dto = new TileDto(tile.getId(), ImageUtils.encode(tile.getImage()));
        fillBasicObjectInfo(dto, tile);
        return dto;
    }

    public ThingDto getThingDTO(Thing thing) {
        LocationDto location = null;
        if (thing.getLocation() != null) {
            location = DTOFactory.instance.getLocationDTO(thing.getLocation());
        }
        ThingDto dto = new ThingDto(thing.getId(), thing.getName(), thing.getDescription(), thing.getScale(), thing.getRotation(), ImageUtils.encode(thing.getImage()), location);
        fillBasicObjectInfo(dto, thing);
        return dto;
    }

    public UserDisconnectedDTO getUserDisconnectedDTO(User user) {
        return new UserDisconnectedDTO(user.getId(), user.getName());
    }

    public GetRoomResultDTO getRoomResultDTO(boolean success, String error, RoomDto dto) {
        return new GetRoomResultDTO(success, error, dto);
    }

    public GetUserResultDTO getGetUserResultDTO(boolean success, String error, UserDto u) {
        return new GetUserResultDTO(success, error, u);
    }

    public GetVerbResultDTO getVerbResultDto(boolean success, String error, VerbDto verb) {
        return new GetVerbResultDTO(success, error, verb);
    }

    public UserInRoomDTO getUserInRoomDTO(User user, Location location) {
        UserDto userDto = getUserDTO(user);
        LocationDto locationDto = getLocationDTO(location);
        return new UserInRoomDTO(userDto, locationDto);
    }

    public UserEnteredRoomDTO getUserEnteredRoomDTO(User user, Location location, boolean teleported) {
        UserDto userDto = getUserDTO(user);
        LocationDto locationDto = getLocationDTO(location);
        return new UserEnteredRoomDTO(userDto, locationDto, teleported);
    }

    public VerbDeletedDTO getVerbDeletedDTO(Verb verb) {
        return new VerbDeletedDTO(verb.getId());
    }

    public RoomDeletedDTO getRoomDeletedDTO(Room room) {
        return new RoomDeletedDTO(room.getId());
    }

    public RoomUpdatedDTO getRoomUpdatedDTO(RoomDto roomDTO) {
        return new RoomUpdatedDTO(roomDTO);
    }

    public GetTileResultDTO getGetTileResultDTO(boolean success, String message, TileDto tile) {
        return new GetTileResultDTO(success, message, tile);
    }

    public RoomAddedDTO getRoomAddedDTO(RoomDto roomDTO) {
        return new RoomAddedDTO(roomDTO);
    }

    public AvatarAddedDTO getAvatarAddedDTO(AvatarDto avatarDto) {
        return new AvatarAddedDTO(avatarDto);
    }

    public GetAvatarResultDTO getGetAvatarResultDTO(boolean success, String message, Long userId, AvatarDto avatarDto) {
        return new GetAvatarResultDTO(success, message, userId, avatarDto);
    }



}


