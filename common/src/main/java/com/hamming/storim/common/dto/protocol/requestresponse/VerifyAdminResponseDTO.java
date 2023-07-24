package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class VerifyAdminResponseDTO extends ResponseDTO {

    private boolean success = false;
    private String errorMessage;

    public VerifyAdminResponseDTO(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "VerifyAdminResponseDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
