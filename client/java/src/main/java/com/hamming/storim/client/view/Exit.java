package com.hamming.storim.client.view;

import com.hamming.storim.client.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Exit extends BasicDrawableObject {

    private float scale = 1;
    private int rotation = 0;
    private BufferedImage scaledImage;

    private String name;

    private String toRoomURI;


    public Exit(Long exitId, String name) {
        setId(exitId);
        this.scaledImage = null;
        this.toRoomURI = null;
        this.name = name;
    }

    public void setScale(float scale) {
        this.scale = scale;
        updateScaledImage();
    }


    public void setRotation(int rotation) {
        this.rotation = rotation;
        updateScaledImage();
    }

    private void updateScaledImage() {
        BufferedImage image = (BufferedImage) super.getImage();
        scaledImage = ImageUtils.scaleImage(image, scale);
        scaledImage = ImageUtils.rotateImage(scaledImage, rotation);
    }

    @Override
    public Image getImage() {
        return scaledImage;
    }

    @Override
    public void setImage(Image image) {
        super.setImage(image);
        updateScaledImage();
    }

    public String getName() {
        return name;
    }

    public String getToRoomURI() {
        return toRoomURI;
    }

    public void setToRoomURI(String toRoomURI) {
        this.toRoomURI = toRoomURI;
    }
}
