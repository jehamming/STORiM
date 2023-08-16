package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetUserResultDTO extends ResponseDTO {

    private UserDto user;

    public GetUserResultDTO(boolean success, UserDto user, String errorMessage) {
        super(success, errorMessage);
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }


    @Override
    public String toString() {
        return "GetUserResultDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", user=" + user +
                '}';
    }
}
                                                                                                                                          