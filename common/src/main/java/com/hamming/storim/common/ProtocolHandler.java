package com.hamming.storim.common;

import com.hamming.storim.common.dto.protocol.requestresponse.LoginRequestDTO;
import com.hamming.storim.common.dto.protocol.request.TeleportRequestDTO;
import com.hamming.storim.common.dto.protocol.request.AddAvatarDto;
import com.hamming.storim.common.dto.protocol.request.AddRoomDto;
import com.hamming.storim.common.dto.protocol.request.DeleteRoomDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateRoomDto;
import com.hamming.storim.common.dto.protocol.requestresponse.AddThingDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetUserDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.AddVerbDto;
import com.hamming.storim.common.dto.protocol.requestresponse.DeleteVerbDTO;
import com.hamming.storim.common.dto.protocol.request.ExecVerbDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateVerbDto;
import com.hamming.storim.common.util.StringUtils;

public class ProtocolHandler implements Protocol {

    private static ProtocolHandler instance;

    private ProtocolHandler() {
    }

    public static ProtocolHandler getInstance() {
        if (instance == null) {
            instance = new ProtocolHandler();
        }
        return instance;
    }


    public LoginRequestDTO getLoginDTO(String username, String password) {
        String hashedPassword = StringUtils.hashPassword(password);
        LoginRequestDTO dto = new LoginRequestDTO(username, hashedPassword);
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

    public AddVerbDto getAddVerbDTO(String name, String toCaller, String toLocation) {
        return new AddVerbDto(name, toCaller, toLocation);
    }

    public UpdateVerbDto getUpdateVerbDTO(Long id, String name, String toCaller, String toLocation) {
        return new UpdateVerbDto(id, name, toCaller, toLocation);
    }

    public AddRoomDto getAddRoomDTO(String roomName, Long tileID, byte[] imageData) {
        return new AddRoomDto(roomName, tileID, imageData);
    }

    public UpdateRoomDto getUpdateRoomDto(Long roomId, String roomName, int width, int length, int rows, int cols, Long tileID, byte[] imageData) {
        return new UpdateRoomDto(roomId, roomName, width, length, rows, cols, tileID, imageData);
    }

    public DeleteRoomDTO getDeleteRoomDto(Long roomId) {
        return new DeleteRoomDTO(roomId);
    }

    public AddAvatarDto getAddAvatarDTO(String avatarName, byte[] avatarImage) {
        return new AddAvatarDto(avatarName, avatarImage);
    }

    public AddThingDto getAddThingDTO(String name, String description, Float scale, int rotation, byte[] image) {
        return new AddThingDto(name, description, scale, rotation, image);
    }
}
