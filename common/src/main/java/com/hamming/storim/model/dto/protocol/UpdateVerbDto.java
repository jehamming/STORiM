package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

public class UpdateVerbDto implements ProtocolDTO {

    private String name;
    private String shortName;
    private String toCaller;
    private String toLocation;
    private Long verbId;


    public UpdateVerbDto(Long verbId, String name, String shortName, String toCaller, String toLocation){
        this.name = name;
        this.shortName = shortName;
        this.toCaller = toCaller;
        this.toLocation = toLocation;
        this.verbId = verbId;
    }


    public String getName() {
        return name;

    }

    public Long getVerbId() {
        return verbId;
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
        return "UpdateVerbDto{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", toCaller='" + toCaller + '\'' +
                ", toLocation='" + toLocation + '\'' +
                ", verbId=" + verbId +
                '}';
    }
}
