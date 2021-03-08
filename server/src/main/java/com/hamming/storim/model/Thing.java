package com.hamming.storim.model;

public class Thing extends BasicObject   {

    private String name;

    public Thing(Long id) {
        super(id);
        name = "A basic thing";
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
        return "Thing{" +
                "name=" + name +
                '}';
    }
}
