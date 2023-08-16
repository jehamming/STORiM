package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class UserUpdatedDTO extends ResponseDTO {

    private UserDto user;

    public UserUpdatedDTO( UserDto user) {
        super(true, null);
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
                                                                                                                                          