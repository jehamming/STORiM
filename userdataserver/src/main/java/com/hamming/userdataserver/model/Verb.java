package com.hamming.userdataserver.model;

import com.hamming.storim.server.common.model.BasicObject;

public class Verb extends BasicObject {

    private String name = "An empty command";
    private String toCaller;
    private String toLocation;

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
