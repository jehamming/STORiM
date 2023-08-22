package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class LoginWithTokenResultDTO extends ResponseDTO {

    public LoginWithTokenResultDTO(boolean success, String errorMessage) {
        super(success, errorMessage);
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
