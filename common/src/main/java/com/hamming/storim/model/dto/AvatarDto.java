package com.hamming.storim.model.dto;

import com.hamming.storim.util.ImageUtils;

import java.awt.*;
import java.util.Arrays;

public class AvatarDto extends DTO {

    private byte[] imageData;
    private transient Image image;


    public AvatarDto(Long id, String name, byte[] imageData){
        setId(id);
        setName(name);
        this.imageData = imageData;
    }

    public Image getImage() {
        if ( image == null ) {
            image = ImageUtils.decode(imageData);
        }
        return image;
    }

    @Override
    public String toString() {
        return "AvatarDto{ id = "+ getId()+ "}";
    }
}
