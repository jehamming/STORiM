package com.hamming.storim.game;

import com.hamming.storim.model.dto.protocol.*;
import com.hamming.storim.util.ImageUtils;
import com.hamming.storim.util.StringUtils;

import java.awt.*;

public class ProtocolHandler implements Protocol {



    public LoginRequestDTO getLoginDTO(String username, String password) {
        String hashedPassword = StringUtils.hashPassword(password);
        LoginRequestDTO dto = new LoginRequestDTO(username, hashedPassword);
        return dto;
    }

    public VersionCheckDTO getVersionCheckDTO(String clientVersion) {
        VersionCheckDTO dto = new VersionCheckDTO(clientVersion);
        return dto;
    }

    public TeleportRequestDTO getTeleportRequestDTO(Long userId, Long roomId) {
        TeleportRequestDTO dto = new TeleportRequestDTO(userId, roomId);
        return dto;
    }

    public GetUserDTO getUserDTO(Long userId) {
        GetUserDTO dto = new GetUserDTO(userId);
        return dto;
    }

    public ExecVerbDTO getExecVerbDTO(Long commandId, String input) {
        ExecVerbDTO dto = new ExecVerbDTO(commandId, input);
        return dto;
    }

    public DeleteVerbDTO getDeleteVerbDTO(Long id) {
        return new DeleteVerbDTO(id);
    }

    public AddVerbDto getAddVerbDTO(String name, String shortName, String toCaller, String toLocation) {
        return new AddVerbDto(name, shortName, toCaller, toLocation);
    }

    public UpdateVerbDto getUpdateVerbDTO(Long id, String name, String shortName, String toCaller, String toLocation) {
        return new UpdateVerbDto(id, name, shortName, toCaller, toLocation);
    }

    public AddRoomDto getAddRoomDTO(String roomName, int roomSize, Long tileID, Image image) {
        byte[] imageData = null;
        if ( image != null ) {
            imageData = ImageUtils.encode(image);
        }
        return new AddRoomDto(roomName, roomSize, tileID, imageData);
    }

    public UpdateRoomDto getUpdateRoomDto(Long roomId, String roomName, int roomSize, Long tileID, Image image) {
        byte[] imageData = null;
        if (image != null ) {
            imageData = ImageUtils.encode(image);
        }
        return new UpdateRoomDto(roomId, roomName, roomSize, tileID, imageData);
    }

    public DeleteRoomDTO getDeleteRoomDto(Long roomId) {
        return new DeleteRoomDTO(roomId);
    }
}
