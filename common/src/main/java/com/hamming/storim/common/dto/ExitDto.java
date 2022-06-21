package com.hamming.storim.common.dto;

import java.util.Arrays;

public class ExitDto extends DTO {


    private Long toRoomId;
    private String toServerID;
    private String description;
    private byte[] imageData;
    private float scale;
    private int rotation;

    public ExitDto(Long id, String name, String toServerID, Long toRoomId, String description, float scale, int rotation, byte[] imageData) {
        setId(id);
        setName(name);
        this.toRoomId = toRoomId;
        this.description = description;
        this.imageData = imageData;
        this.scale = scale;
        this.rotation = rotation;
        this.toServerID = toServerID;
    }

    public Long getToRoomId() {
        return toRoomId;
    }

    public String getToServerID() {
        return toServerID;
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

    @Override
    public String toString() {
        return "ExitDto{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", toRoomId=" + toRoomId +
                ", toServerID='" + toServerID + '\'' +
                ", description='" + description + '\'' +
                ", scale=" + scale +
                ", rotation=" + rotation +
                '}';
    }
}

