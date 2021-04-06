package com.hamming.storim.server.model;

public class Thing extends ImageObject   {

    private String description;
    private float scale;
    private int rotation;
    private Location location;

    public Thing(Long id) {
        super(id);
    }

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Thing{" +
                "id=" + getId() +
                ", description='" + description + '\'' +
                ", scale=" + scale +
                ", rotation=" + rotation +
                ", location=" + location +
                '}';
    }
}
