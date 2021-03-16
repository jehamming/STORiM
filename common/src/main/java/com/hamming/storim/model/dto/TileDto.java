package com.hamming.storim.model.dto;

import com.hamming.storim.util.ImageUtils;

import java.awt.*;

public class TileDto extends DTO {

    private byte[] imageData;
    private transient Image image;


    public TileDto(Long id, byte[] imageData){
        setId(id);
        this.imageData = imageData;
    }

    public Image getImage() {
        if ( image == null ) {
            image = ImageUtils.decode(imageData);
        }
        return image;
    }

}
