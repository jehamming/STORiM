package com.hamming.storim.server.common.dto.protocol.dataserver.user;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.RequestResponseDTO;

public class GetUserRequestDTO extends RequestResponseDTO {

    private UserDto user;

    public GetUserRequestDTO(UserDto user) {
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "GetUserDTO{" +
                "user=" + user +
                '}';
    }
}
