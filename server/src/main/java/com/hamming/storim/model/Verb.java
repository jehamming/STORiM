package com.hamming.storim.model;

public class Verb extends BasicObject   {

    private String name;
    private String toCaller;
    private String toLocation;

    public Verb(Long id) {
        super(id);
        name = "An empty command";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getToCaller() {
        return toCaller;
    }

    public void setToCaller(String toCaller) {
        this.toCaller = toCaller;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    @Override
    public String toString() {
        return "Verb{" +
                " id=" + getId() +
                ", name='" + name + '\'' +
                ", toCaller='" + toCaller + '\'' +
                ", toLocation='" + toLocation + '\'' +
                '}';
    }
}
