package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class GetUserResultDTO extends ResponseDTO {

    private boolean success = false;
    private String errorMessage;
    private UserDto user;

    public GetUserResultDTO(boolean success, UserDto user, String errorMessage) {
        this.user = user;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public UserDto getUser() {
        return user;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "GetUserResultDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", user=" + user +
                '}';
    }
}
                                                                                                                                          