package com.hamming.storim.common.dto.protocol.serverpush.old;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class UserUpdatedDTO implements  ResponseDTO {

    private UserDto user;

    public UserUpdatedDTO( UserDto user) {
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "UserUpdatedDTO{" +
                "user=" + user +
                '}';
    }
}
                                                                                                                                          