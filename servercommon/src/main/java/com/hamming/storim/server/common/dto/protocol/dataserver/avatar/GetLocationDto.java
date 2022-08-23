package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class GetLocationDto extends ProtocolDTO {

    private Long objectId;


    public GetLocationDto(Long objectId) {
        this.objectId = objectId;
    }

    public Long getObjectId() {
        return objectId;
    }

    @Override
    public String toString() {
        return "GetLocationDto{" +
                "objectId=" + objectId +
                '}';
    }
}
