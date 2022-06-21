package com.hamming.userdataserver;


import com.hamming.storim.common.dto.*;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.model.BasicObject;
import com.hamming.storim.server.common.model.Location;
import com.hamming.userdataserver.model.*;

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
        ThingDto dto = new ThingDto(thing.getId(), thing.getName(), thing.getDescription(), thing.getScale(), thing.getRotation(), ImageUtils.encode(thing.getImage()));
        fillBasicObjectInfo(dto, thing);
        return dto;
    }

    public LocationDto getLocationDTO(Location l) {
        LocationDto dto = new LocationDto(l.getObjectId(), l.getServerId(), l.getRoomId(), l.getX(), l.getY());
        fillBasicObjectInfo(dto, l);
        return dto;
    }


}


