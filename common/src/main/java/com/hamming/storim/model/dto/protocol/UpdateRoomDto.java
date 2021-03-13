package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

import java.awt.*;

public class UpdateRoomDto implements ProtocolDTO {

    private Long roomId;
    private String name;
    private Integer size;
    private Long tileId;
    private byte[] imageData;

    public UpdateRoomDto(Long id, String name, Integer size, Long tileId, byte[] imageData){
        this.name = name;
        this.size = size;
        this.roomId = id;
        this.tileId = tileId;
        this.imageData = imageData;
    }


    public String getName() {
        return name;

    }

    public Long getRoomId() {
        return roomId;
    }

    public Integer getSize() {
        return size;
    }

    public Long getTileId() {
        return tileId;
    }

    public byte[] getImageData() {
        return imageData;
    }

    @Override
    public String toString() {
        return "UpdateRoomDto{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", tileId=" + tileId +
                '}';
    }
}
