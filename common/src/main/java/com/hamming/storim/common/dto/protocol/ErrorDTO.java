package com.hamming.storim.common.dto.protocol;

public class ErrorDTO extends ResponseDTO {

    private String function;

    public ErrorDTO(String function, String errorMessage) {
        super(false, errorMessage);
        this.function = function;
    }

    public String getFunction() {
        return function;
    }

    @Override
    public String toString() {
        return "ErrorDTO{" +
                "function='" + function + '\'' +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}
