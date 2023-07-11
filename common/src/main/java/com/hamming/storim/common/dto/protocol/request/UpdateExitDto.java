package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.util.Arrays;

public class UpdateExitDto extends ProtocolDTO {

    private Long id;
    private String name;
    private String description;

    private Long toRoomId;
    private String toRoomURI;
    private float scale;
    private int rotation;
    private byte[] imageData;


    public UpdateExitDto(Long id, String name, Long toRoomId, String toRoomURI, String description, float scale, int rotation, byte[] imageData){
        this.id = id;
        this.name = name;
        this.imageData = imageData;
        this.description = description;
        this.scale = scale;
        this.rotation = rotation;
        this.toRoomId = toRoomId;
        this.toRoomURI = toRoomURI;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;

    }

    public String getDescription() {
        return description;
    }

    public float getScale() {
        return scale;
    }

    public int getRotation() {
        return rotation;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public String getToRoomURI() {
        return toRoomURI;
    }

    public Long getToRoomId() {
        return toRoomId;
    }

    @Override
    public String toString() {
        return "UpdateExitDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", toRoomId=" + toRoomId +
                ", toRoomURI='" + toRoomURI + '\'' +
                ", scale=" + scale +
                ", rotation=" + rotation +
                '}';
    }
}
