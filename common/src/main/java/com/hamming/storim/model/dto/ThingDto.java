package com.hamming.storim.model.dto;

import com.hamming.storim.util.ImageUtils;

import java.awt.*;

public class ThingDto extends DTO {

    private byte[] imageData;
    private transient Image image;
    private String description;
    private float scale;
    private float rotation;

    public ThingDto(Long id, String name, String description, float scale, float rotation, byte[] imageData ){
        setId(id);
        setName(name);
        this.imageData = imageData;
        this.description = description;
        this.scale = scale;
        this.rotation = rotation;
    }

    public Image getImage() {
        if ( image == null ) {
            image = ImageUtils.decode(imageData);
        }
        return image;
    }

    public byte[] getImageData() {
        return imageData;
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

    @Override
    public String toString() {
        return "ThingDto{" +
                "description='" + description + '\'' +
                ", scale=" + scale +
                ", rotation=" + rotation +
                '}';
    }
}
