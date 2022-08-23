package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class UpdateThingDto extends ProtocolDTO {

    private Long id;
    private String name;
    private String description;
    private float scale;
    private int rotation;
    private byte[] imageData;


    public UpdateThingDto(Long id, String name, String description, float scale, int rotation,  byte[] imageData){
        this.id = id;
        this.name = name;
        this.imageData = imageData;
        this.description = description;
        this.scale = scale;
        this.rotation = rotation;
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

    @Override
    public String toString() {
        return "UpdateThingDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", scale=" + scale +
                ", rotation=" + rotation +
                '}';
    }
}
