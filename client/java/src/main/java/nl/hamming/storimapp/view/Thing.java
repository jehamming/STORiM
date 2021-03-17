package nl.hamming.storimapp.view;

import java.awt.*;
import java.util.Objects;

public class Thing {

    private Long thingId;
    private Image image;
    private float scale, rotation;
    private int x, y;

    public Thing(Long thingId) {
        this.thingId = thingId;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Long getThingId() {
        return thingId;
    }

    public void setThingId(Long thingId) {
        this.thingId = thingId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Thing thing = (Thing) o;
        return Objects.equals(thingId, thing.thingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(thingId);
    }
}
