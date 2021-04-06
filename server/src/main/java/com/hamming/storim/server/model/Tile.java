package com.hamming.storim.server.model;

public class Tile extends ImageObject   {

    public Tile(Long id) {
        super(id);
    }
    @Override
    public String toString() {
        return "Tile{" +
                "id=" + getId() +
                '}';
    }

}
