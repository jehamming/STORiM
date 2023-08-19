package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetUserByUsernameResultDTO extends ResponseDTO {

    private UserDto user;

    public GetUserByUsernameResultDTO(boolean success, UserDto user, String errorMessage) {
        super(success, errorMessage);
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }


    @Override
    public String toString() {
        return "GetUserByUsernameResultDTO{" +
                "user=" + user +
                '}';
    }
}
                                                                                                                                          