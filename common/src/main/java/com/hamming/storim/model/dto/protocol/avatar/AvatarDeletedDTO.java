package com.hamming.storim.model.dto.protocol.avatar;

import com.hamming.storim.model.dto.protocol.ProtocolDTO;

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
