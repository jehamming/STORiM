package com.hamming.storim.model;

public class Tile extends BasicObject   {

    private String name;

    public Tile(Long id) {
        super(id);
        name = "A basic tile";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Room{" +
                "name=" + name +
                '}';
    }
}
