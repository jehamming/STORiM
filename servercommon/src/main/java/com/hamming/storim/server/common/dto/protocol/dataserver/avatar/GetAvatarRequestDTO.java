package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetAvatarRequestDTO extends ProtocolDTO {
    private Long avatarId;
    public GetAvatarRequestDTO(Long avatarId) {
        this.avatarId = avatarId;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    @Override
    public String toString() {
        return "GetAvatarRequestDTO{" +
                "avatarId=" + avatarId +
                '}';
    }
}
