package com.hamming.storim.model;

import java.awt.*;


public abstract class ImageObject extends BasicObject {

    private transient Image image;

    public ImageObject(Long id) {
        super(id);
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

}
