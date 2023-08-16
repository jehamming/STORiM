package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.List;

public class GetAvatarsResponseDTO extends ResponseDTO {

    private final List<Long> avatars;

    public GetAvatarsResponseDTO(boolean success, String errorMessage, List<Long> avatars) {
        super(success, errorMessage);
        this.avatars = avatars;
    }

    public List<Long> getAvatars() {
        return avatars;
    }

    @Override
    public String toString() {
        return "GetAvatarsResponseDTO{" +
                "avatars=" + avatars +
                ", success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}
