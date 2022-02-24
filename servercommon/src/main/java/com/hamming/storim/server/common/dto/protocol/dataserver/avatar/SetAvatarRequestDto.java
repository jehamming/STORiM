package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class SetAvatarRequestDto implements ProtocolDTO {

    private Long avatarId;
    private Long userId;

    public SetAvatarRequestDto(Long avatarId, Long userId) {
        this.avatarId = avatarId;
        this.userId = userId;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "SetAvatarRequestDto{" +
                "avatarId=" + avatarId +
                ", userId=" + userId +
                '}';
    }
}
