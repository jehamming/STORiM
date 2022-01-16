package com.hamming.storim.client.view;

import java.awt.*;
import java.util.Objects;

public class BasicDrawableObject {
    private boolean selected = false;
    private Long id;
    private Image image;
    private int x, y;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicDrawableObject thing = (BasicDrawableObject) o;
        return Objects.equals(getId(), thing.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public boolean withinBounds(int x, int y){
        int middleX = getImage().getWidth(null) / 2;
        int middleY = getImage().getHeight(null) / 2;
        int startX = getX() - middleX;
        int endX = startX + getImage().getWidth(null);
        int startY = getY() - middleY;
        int endY = startY + getImage().getHeight(null);
        return x >= startX && x <= endX && y >= startY && y <= endY;
    }



}
