package com.hamming.storim.model.dto.protocol.user;

import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class UserUpdatedDTO implements ProtocolDTO {

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
                                                                                                                                          