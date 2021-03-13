package com.hamming.storim.model.dto;

import com.hamming.storim.util.ImageUtils;

import java.awt.*;

public class TileDto implements DTO {

    private Long id;
    private byte[] imageData;
    private transient Image image;


    public TileDto(Long id, byte[] imageData){
        this.id = id;
        this.imageData = imageData;
    }

    public Long getId() {
        return id;
    }

    public Image getImage() {
        if ( image == null ) {
            image = ImageUtils.decode(imageData);
        }
        return image;
    }

    @Override
    public String toString() {
        return "RoomTileDto{" +
                "id=" + id +
                '}';
    }
}
