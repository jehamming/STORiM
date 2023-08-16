package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.HashMap;
import java.util.List;

public class GetAvatarsResponseDTO extends ResponseDTO {

    private final List<Long> avatars;

    public GetAvatarsResponseDTO(boolean success, List<Long> avatars, String errorMessage) {
        super(success, errorMessage);
        this.avatars = avatars;
    }

    public List<Long> getAvatars() {
        return avatars;
    }


    @Override
    public String toString() {
        return "GetAvatarsResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", avatars=" + avatars +
                '}';
    }
}
