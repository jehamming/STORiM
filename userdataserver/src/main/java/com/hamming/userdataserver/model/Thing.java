package com.hamming.userdataserver.model;

import com.hamming.storim.server.common.ImageObject;

public class Thing extends ImageObject {

    private String description;
    private float scale;
    private int rotation;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    @Override
    public String toString() {
        return "Thing{" +
                "id=" + getId() +
                ", description='" + description + '\'' +
                ", scale=" + scale +
                ", rotation=" + rotation +
                '}';
    }
}
