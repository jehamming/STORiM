package com.hamming.storim.factories;

import com.hamming.storim.model.Location;
import com.hamming.storim.model.Room;
import com.hamming.storim.model.User;
import com.hamming.storim.model.Verb;
import com.hamming.storim.model.dto.*;
import com.hamming.storim.model.dto.protocol.*;

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


    public UserDto getUserDTO(User u) {
        UserDto dto = new UserDto(u.getId(), u.getName(), u.getEmail());
        return dto;
    }


    public LocationDto getLocationDTO(Location loc) {
        return new LocationDto(loc.getRoom().getId(), loc.getX(), loc.getY());
    }

    public RoomDto getRoomDto(Room b) {
        RoomDto dto = new RoomDto(b.getId(), b.getName(), b.getCreator().getId(), b.getOwner().getId(), b.getSize());
        return dto;
    }

    public VerbDto getVerbDto(Verb c) {
        VerbDto dto = new VerbDto(c.getId(), c.getCreator().getId().toString(), c.getOwner().getId().toString(), c.getName(), c.getShortName(), c.getToCaller(), c.getToLocation());
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

    public DTO getRoomDeletedDTO(Room room) {
        return new RoomDeletedDTO(room.getId());
    }
}
