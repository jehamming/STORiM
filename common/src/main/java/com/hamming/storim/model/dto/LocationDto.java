package com.hamming.storim.model.dto;

import com.hamming.storim.util.StringUtils;

public class LocationDto implements DTO {

    private Long id;
    private Long roomId;
    private int x,y;

    public LocationDto(Long roomId, int x, int y){
        this.roomId = roomId;
        this.x = x;
        this.y = y;
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "LocationDto{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
