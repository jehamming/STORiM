package com.hamming.storim.common;

import com.hamming.storim.common.dto.protocol.requestresponse.LoginDTO;
import com.hamming.storim.common.dto.protocol.request.AddVerbDto;
import com.hamming.storim.common.dto.protocol.request.DeleteVerbDTO;
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


    public LoginDTO getLoginDTO(String username, String password) {
        String hashedPassword = StringUtils.hashPassword(password);
        LoginDTO dto = new LoginDTO(username, hashedPassword);
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


}
