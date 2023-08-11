package com.hamming.storim.server.common.model;

import com.hamming.storim.server.common.ImageObject;

import java.awt.*;

public class TileSet extends ImageObject {

    private int tileWidth;
    private int tileHeight;

    public TileSet(Long id, String name,  Image image, int tileWidth, int tileHeight) {
        setId(id);
        setImage(image);
        setName(name);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    @Override
    public String toString() {
        return "TileSet{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", tileWidth=" + tileWidth +
                ", tileHeight=" + tileHeight +
                '}';
    }
}
