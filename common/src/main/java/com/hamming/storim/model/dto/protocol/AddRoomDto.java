package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

import java.util.Arrays;

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
