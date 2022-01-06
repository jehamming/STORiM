package com.hamming.storim.common;

import com.hamming.storim.common.dto.protocol.login.LoginRequestDTO;
import com.hamming.storim.common.dto.protocol.TeleportRequestDTO;
import com.hamming.storim.common.dto.protocol.avatar.AddAvatarDto;
import com.hamming.storim.common.dto.protocol.room.AddRoomDto;
import com.hamming.storim.common.dto.protocol.room.DeleteRoomDTO;
import com.hamming.storim.common.dto.protocol.room.UpdateRoomDto;
import com.hamming.storim.common.dto.protocol.thing.AddThingDto;
import com.hamming.storim.common.dto.protocol.user.GetUserDTO;
import com.hamming.storim.common.dto.protocol.verb.AddVerbDto;
import com.hamming.storim.common.dto.protocol.verb.DeleteVerbDTO;
import com.hamming.storim.common.dto.protocol.verb.ExecVerbDTO;
import com.hamming.storim.common.dto.protocol.verb.UpdateVerbDto;
import com.hamming.storim.common.util.StringUtils;

public class ProtocolHandler implements Protocol {



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
