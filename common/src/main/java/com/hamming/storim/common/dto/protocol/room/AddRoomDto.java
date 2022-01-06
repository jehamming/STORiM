package com.hamming.storim.common.dto.protocol.room;

import com.hamming.storim.common.dto.protocol.ProtocolASyncRequestDTO;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class AddRoomDto extends ProtocolASyncRequestDTO {

    private String name;
    private Long tileId;
    private byte[] imageData;

    public AddRoomDto(String name, Long tileId, byte[] imageData){
        this.name = name;
        this.tileId = tileId;
        this.imageData = imageData;
    }

    public String getName() {
        return name;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public Long getTileId() {
        return tileId;
    }

    @Override
    public String toString() {
        return "AddRoomDto{" +
                "name='" + name + '\'' +
                ", tileId=" + tileId +
                '}';
    }
}
