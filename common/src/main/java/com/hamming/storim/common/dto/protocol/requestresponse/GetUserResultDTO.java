package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetUserResultDTO implements  ResponseDTO {

    private UserDto user;

    public GetUserResultDTO(UserDto user) {
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "GetUserResultDTO{" +
                "user=" + user +
                '}';
    }
}
                                                                                                                                          