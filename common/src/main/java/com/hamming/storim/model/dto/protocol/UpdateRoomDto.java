package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

public class UpdateRoomDto implements DTO {

    private Long roomId;
    private String name;
    private Integer size;

    public UpdateRoomDto(Long id, String name, Integer size){
        this.name = name;
        this.size = size;
        this.roomId = id;
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

    @Override
    public String toString() {
        return "UpdateRoomDto{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
