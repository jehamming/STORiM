package com.hamming.storim.model;

import java.awt.*;

public class Tile extends BasicObject   {

    // Transient, dus wordt niet opgeslagen
    private transient Image image;

    public Tile(Long id) {
        super(id);
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }


}
