package com.hamming.storim.common.dto.protocol.avatar;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolResponseDTO;

public class AvatarDeletedDTO extends ProtocolResponseDTO {

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