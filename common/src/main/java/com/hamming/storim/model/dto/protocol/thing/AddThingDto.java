package com.hamming.storim.model.dto.protocol.thing;

import com.hamming.storim.model.dto.protocol.ProtocolDTO;

public class AddThingDto implements ProtocolDTO {

    private String name;
    private String description;
    private float scale;
    private float rotation;
    private byte[] imageData;

    public AddThingDto(String name, String description, float scale, float rotation,  byte[] imageData){
        this.name = name;
        this.imageData = imageData;
        this.description = description;
        this.scale = scale;
        this.rotation = rotation;
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

    public float getRotation() {
        return rotation;
    }

    public byte[] getImageData() {
        return imageData;
    }

    @Override
    public String toString() {
        return "AddThingDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", scale=" + scale +
                ", rotation=" + rotation +
                '}';
    }
}
