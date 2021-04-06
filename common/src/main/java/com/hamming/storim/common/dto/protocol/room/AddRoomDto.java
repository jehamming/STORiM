package com.hamming.storim.common.dto.protocol.room;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class AddRoomDto implements ProtocolDTO {

    private String name;
    private Integer size;
    private Long tileId;
    private byte[] imageData;

    public AddRoomDto(String name, Integer size, Long tileId, byte[] imageData){
        this.name = name;
        this.size = size;
        this.tileId = tileId;
        this.imageData = imageData;
    }

    public String getName() {
        return name;

    }

    public Integer getSize() {
        return size;
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
                ", size=" + size +
                ", tileId=" + tileId +
                '}';
    }
}
