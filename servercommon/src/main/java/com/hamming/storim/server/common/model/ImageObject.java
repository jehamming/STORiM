package com.hamming.storim.server.common.model;

import java.awt.*;


public abstract class ImageObject extends BasicObject {

    private transient Image image;

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

}
