package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

public class AddRoomDto implements DTO {

    private String name;
    private Integer size;

    public AddRoomDto(String name, Integer size){
        this.name = name;
        this.size = size;
    }


    public String getName() {
        return name;

    }

    public Integer getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "AddRoomDto{" +
                "name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
