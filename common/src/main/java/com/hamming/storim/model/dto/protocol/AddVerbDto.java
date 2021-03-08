package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

public class AddVerbDto implements DTO {

    private String name;
    private String shortName;
    private String toCaller;
    private String toLocation;


    public AddVerbDto(String name, String shortName, String toCaller, String toLocation){
        this.name = name;
        this.shortName = shortName;
        this.toCaller = toCaller;
        this.toLocation = toLocation;
    }


    public String getName() {
        return name;

    }

    public String getShortName() {
        return shortName;
    }

    public String getToCaller() {
        return toCaller;
    }

    public String getToLocation() {
        return toLocation;
    }



    @Override
    public String toString() {
        return "AddVerbDto{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", toCaller='" + toCaller + '\'' +
                ", toLocation='" + toLocation + '\'' +
                '}';
    }
}
