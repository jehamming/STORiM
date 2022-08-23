package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class AddTileRequestDto extends ProtocolDTO {

    private Long userId;
    private byte[] imageData;

    public AddTileRequestDto(Long userId, byte[] imageData){
        this.imageData = imageData;
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }


    public byte[] getImageData() {
        return imageData;
    }

    @Override
    public String toString() {
        return "AddTileRequestDto{" +
                "userId=" + userId +
                '}';
    }
}
