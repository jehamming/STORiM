package com.hamming.storim.common.dto.protocol.avatar;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class AvatarDeletedDTO implements ProtocolDTO {

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
