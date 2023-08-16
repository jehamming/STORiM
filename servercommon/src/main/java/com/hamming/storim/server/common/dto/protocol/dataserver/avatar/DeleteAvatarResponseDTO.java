package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class DeleteAvatarResponseDTO extends ResponseDTO {

    public DeleteAvatarResponseDTO(boolean success, String errorMessage) {
        super(success, errorMessage);
    }

    @Override
    public String toString() {
        return "DeleteAvatarResponseDTO{" +
                "success=" + isSuccess() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}
