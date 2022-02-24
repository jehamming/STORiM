package com.hamming.userdataserver;


import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserDisconnectedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserEnteredRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.RoomAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.RoomDeletedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.RoomUpdatedDTO;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.model.BasicObject;
import com.hamming.storim.server.common.model.Exit;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.common.model.Room;
import com.hamming.userdataserver.model.*;

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

    public UserDto getUserDTO(User u) {
        Long avatarId = null;
        if (u.getCurrentAvatar() != null ) {
            avatarId  = u.getCurrentAvatar().getId();
        }
        UserDto dto = new UserDto(u.getId(), u.getName(), u.getEmail(), avatarId);
        fillBasicObjectInfo(dto, u);
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
        ThingDto dto = new ThingDto(thing.getId(), thing.getName(), thing.getDescription(), thing.getScale(), thing.getRotation(), ImageUtils.encode(thing.getImage()), location);
        fillBasicObjectInfo(dto, thing);
        return dto;
    }


}


