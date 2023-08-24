package com.hamming.storim.server;


import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.serverpush.UserEnteredRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserInRoomDTO;
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

    private BasicObjectDTO fillBasicObjectInfo(BasicObjectDTO dto, BasicObject basicObject) {
        dto.setCreatorID(basicObject.getCreatorId());
        dto.setName(basicObject.getName());
        dto.setOwnerID(basicObject.getOwnerId());
        dto.setEditors(basicObject.getEditors());
        return dto;
    }



    public LocationDto getLocationDTO(Location l) {
        return new LocationDto(l.getObjectId(), l.getServerId(), l.getRoomId(), l.getX(), l.getY());
    }

    public ExitDto getExitDTO(Exit e) {
        ExitDto exitDto = new ExitDto(e.getId(), e.getName(), e.getToRoomURI(), e.getToRoomID(), e.getDescription(), e.getScale(), e.getRotation(), ImageUtils.encode(e.getImage()), e.getX(), e.getY());
        fillBasicObjectInfo(exitDto, e);
        return exitDto;
    }


    public RoomDto getRoomDto(Room b, String serverURI, boolean editable) {
        List<Long> exits = new ArrayList<>();
        for ( Exit e : b.getExits()) {
            exits.add(e.getId());
        }
        String roomURI = serverURI +"/"+ b.getId();
        RoomDto dto = new RoomDto(b.getId(), roomURI, b.getName(), b.getRows(), b.getCols(), b.getBackTileSetId(), b.getBackTileMap(), b.getFrontTileSetId(), b.getFrontTileMap(), exits, editable);
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

    public TileSetDto getTileSetDTO(TileSet s) {
        byte[] imagedata = ImageUtils.encode(s.getImage());
        TileSetDto dto = new TileSetDto(s.getId(), s.getName(), imagedata, s.getTileWidth(), s.getTileHeight() );
        fillBasicObjectInfo(dto, s);
        return dto;
    }

    public ServerConfigurationDTO getServerConfigurationDTO(ServerConfiguration c) {
        ServerConfigurationDTO dto = new ServerConfigurationDTO(c.getServerName(), c.getDefaultTileSet().getId(), c.getDefaultTile(), c.getDefaultRoom().getId(), c.getServerAdmins());
        return dto;
    }
}


