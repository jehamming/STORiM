package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class VerifyAdminResponseDTO extends ResponseDTO {


    public VerifyAdminResponseDTO(boolean success, String errorMessage) {
        super(success, errorMessage);
    }


    @Override
    public String toString() {
        return "VerifyAdminResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}
