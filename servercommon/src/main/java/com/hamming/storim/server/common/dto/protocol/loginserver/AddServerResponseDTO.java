package com.hamming.storim.server.common.dto.protocol.loginserver;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class AddServerResponseDTO extends ResponseDTO {

    private boolean success;
    private String errorMessage;

    public AddServerResponseDTO(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "AddServerResponseDTO{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
