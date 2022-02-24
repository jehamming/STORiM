package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.util.HashMap;
import java.util.List;

public class GetAvatarsResponseDTO implements  ResponseDTO {

    private final List<Long> avatars;

    public GetAvatarsResponseDTO(List<Long> avatars) {
        this.avatars = avatars;
    }

    public List<Long> getAvatars() {
        return avatars;
    }


    @Override
    public String toString() {
        return "GetAvatarsResponseDTO{" +
                "avatars=" + avatars +
                '}';
    }
}
