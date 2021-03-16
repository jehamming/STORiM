package com.hamming.storim.factories;

import com.hamming.storim.model.*;
import com.hamming.storim.model.dto.*;
import com.hamming.storim.model.dto.protocol.*;
import com.hamming.storim.model.dto.protocol.avatar.AvatarAddedDTO;
import com.hamming.storim.model.dto.protocol.avatar.GetAvatarResultDTO;
import com.hamming.storim.model.dto.protocol.room.GetRoomResultDTO;
import com.hamming.storim.model.dto.protocol.room.RoomAddedDTO;
import com.hamming.storim.model.dto.protocol.room.RoomDeletedDTO;
import com.hamming.storim.model.dto.protocol.room.RoomUpdatedDTO;
import com.hamming.storim.model.dto.protocol.tile.GetTileResultDTO;
import com.hamming.storim.model.dto.protocol.user.GetUserResultDTO;
import com.hamming.storim.model.dto.protocol.verb.ExecVerbResultDTO;
import com.hamming.storim.model.dto.protocol.verb.GetVerbResultDTO;
import com.hamming.storim.model.dto.protocol.verb.VerbDeletedDTO;
import com.hamming.storim.util.ImageUtils;

public class DTOFactory {

    private static DTOFactory instance;

    private DTOFactory() {
    };


    public static DTOFactory getInstance() {
        if ( instance == null ) {
            instance = new DTOFactory();
        }
        return instance;
    }

    private DTO fillBasicObjectInfo(DTO dto, BasicObject basicObject) {
        dto.setCreatorID(basicObject.getCreator().getId());
        dto.setName(basicObject.getName());
        dto.setOwnerID(basicObject.getOwner().getId());
        return dto;
    }

    public UserDto getUserDTO(User u) {
        UserDto dto = null;
        if ( u.getCurrentAvatar() != null ) {
            dto = new UserDto(u.getId(), u.getName(), u.getEmail(), u.getCurrentAvatar().getId());
        } else {
            dto = new UserDto(u.getId(), u.getName(), u.getEmail(), null);
        }
        fillBasicObjectInfo(dto, u);
        return dto;
    }


    public LocationDto getLocationDTO(Location loc) {
        return new LocationDto(loc.getRoom().getId(), loc.getX(), loc.getY());
    }

    public RoomDto getRoomDto(Room b) {
        RoomDto dto;
        if ( b.getTile() != null) {
            dto = new RoomDto(b.getId(), b.getName(), b.getSize(), b.getTile().getId());
        }  else {
            dto = new RoomDto(b.getId(), b.getName(), b.getSize(), null);
        }
        fillBasicObjectInfo(dto, b);
        return dto;
    }

    public VerbDto getVerbDto(Verb c) {
        VerbDto dto = new VerbDto(c.getId(), c.getName(), c.getToCaller(), c.getToLocation());
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

    public UserDisconnectedDTO getUserDisconnectedDTO(Long userId) {
        return new UserDisconnectedDTO(userId);
    }

    public TeleportResultDTO getTeleportResultDTO(boolean success, String error, LocationDto locationDTO, Long fromRoomID) {
        return new TeleportResultDTO(success, error, locationDTO, fromRoomID);
    }

    public UserConnectedDTO getUserConnectedDTO(UserDto userDto, LocationDto location) {
        return new UserConnectedDTO(userDto, location);
    }

    public GetRoomResultDTO getRoomResultDTO(boolean success, String error, RoomDto dto) {
        return new GetRoomResultDTO(success, error, dto);
    }

    public GetUserResultDTO getGetUserResultDTO(boolean success, String error, UserDto u) {
        return new GetUserResultDTO(success,error,u);
    }

    public GetVerbResultDTO getVerbResultDto(boolean success, String error, VerbDto verb) {
        return new GetVerbResultDTO(success, error, verb);
    }

    public ExecVerbResultDTO getExecVerbResultDto(Long verbID, String output) {
        return new ExecVerbResultDTO(verbID, true, null, output);
    }

    public UserLocationUpdateDTO getUserLocationUpdateDTO(User user) {
        LocationDto locationDto = getLocationDTO(user.getLocation());
        return new UserLocationUpdateDTO(user.getId(), locationDto);
    }

    public UserInRoomDTO getUserInRoomDTO(User user, Room room, LocationDto location) {
        return new UserInRoomDTO(user.getId(), room.getId(), location);
    }

    public UserTeleportedDTO getUserTeleportedDTO(User user, Long fromRoomId,  LocationDto location) {
        return new UserTeleportedDTO(user.getId(), fromRoomId, location);
    }

    public UserOnlineDTO getUserOnlineDTO(UserDto userDTO, LocationDto locationDto) {
        return new UserOnlineDTO(userDTO, locationDto);
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
