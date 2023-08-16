package com.hamming.storim.common.dto.protocol;

public class ResponseDTO extends ProtocolDTO {

    private boolean success = false;
    private String errorMessage = "";
    public ResponseDTO(boolean success) {
        this.success = success;
    }
    public ResponseDTO(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
