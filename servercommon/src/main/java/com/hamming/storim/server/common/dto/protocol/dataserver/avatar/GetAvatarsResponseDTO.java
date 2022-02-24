package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.List;

public class GetAvatarsResponseDTO implements  ResponseDTO {

    private final List<Long> avatars;
    private boolean success;
    private String errorMessage;

    public GetAvatarsResponseDTO(boolean success, String errorMessage, List<Long> avatars) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.avatars = avatars;
    }

    public List<Long> getAvatars() {
        return avatars;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "GetAvatarsResponseDTO{" +
                "avatars=" + avatars +
                ", success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
