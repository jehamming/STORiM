package com.hamming.storim.common.dto.protocol;

public class ErrorDTO implements ResponseDTO {

    private String function;
    private String errorMessage;

    public ErrorDTO(String function, String errorMessage) {
        this.function = function;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getFunction() {
        return function;
    }

    @Override
    public String toString() {
        return "ErrorDTO{" +
                "function='" + function + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
