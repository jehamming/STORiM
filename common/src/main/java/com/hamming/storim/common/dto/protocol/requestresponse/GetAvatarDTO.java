package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetAvatarDTO extends ProtocolDTO {
    private Long avatarId;
    public GetAvatarDTO(Long avatarId) {
        this.avatarId = avatarId;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    @Override
    public String toString() {
        return "GetAvatarDTO{" +
                "avatarId=" + avatarId +
                '}';
    }
}
