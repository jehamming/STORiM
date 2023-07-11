package com.hamming.storim.common.dto;

import java.util.Arrays;

public class ExitDto extends DTO {


    private Long toRoomId;
    private String toRoomURI;
    private String description;
    private byte[] imageData;
    private float scale;
    private int rotation;
    private int x,y;

    public ExitDto(Long id, String name, String toRoomURI, Long toRoomId, String description, float scale, int rotation, byte[] imageData,int x, int y) {
        setId(id);
        setName(name);
        this.toRoomId = toRoomId;
        this.description = description;
        this.imageData = imageData;
        this.scale = scale;
        this.rotation = rotation;
        this.toRoomURI = toRoomURI;
        this.x = x;
        this.y = y;
    }

    public Long getToRoomId() {
        return toRoomId;
    }

    public String getToRoomURI() {
        return toRoomURI;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public float getScale() {
        return scale;
    }

    public int getRotation() {
        return rotation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "ExitDto{" +
                "toRoomId=" + toRoomId +
                ", toRoomURI='" + toRoomURI + '\'' +
                ", description='" + description + '\'' +
                ", scale=" + scale +
                ", rotation=" + rotation +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}

