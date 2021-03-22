package com.hamming.storim.model.dto;

import com.hamming.storim.util.ImageUtils;

import java.awt.*;

public class ThingDto extends DTO {

    private byte[] imageData;
    private transient Image image;
    private String description;
    private float scale;
    private int rotation;
    private LocationDto location;

    public ThingDto(Long id, String name, String description, float scale, int rotation, byte[] imageData, LocationDto location ){
        setId(id);
        setName(name);
        this.imageData = imageData;
        this.description = description;
        this.scale = scale;
        this.rotation = rotation;
        this.location = location;
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

    public int getRotation() {
        return rotation;
    }

    public LocationDto getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "ThingDto{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", description='" + description + '\'' +
                ", scale=" + scale +
                ", rotation=" + rotation +
                ", location=" + location +
                '}';
    }
}
