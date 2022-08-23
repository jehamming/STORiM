package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class AvatarDeletedDTO extends ResponseDTO {

    private Long avatarId;

    public AvatarDeletedDTO(Long avatarId) {
        this.avatarId = avatarId;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    @Override
    public String toString() {
        return "AvatarDeletedDTO{" +
                "avatarId=" + avatarId +
                '}';
    }
}
