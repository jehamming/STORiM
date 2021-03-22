package com.hamming.storim.model.dto.protocol.thing;

import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class UpdateThingLocationDto implements ProtocolDTO {

    private Long id;
    private int x;
    private int y;


    public UpdateThingLocationDto(Long id, int x, int y){
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "UpdateThingLocationDto{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
