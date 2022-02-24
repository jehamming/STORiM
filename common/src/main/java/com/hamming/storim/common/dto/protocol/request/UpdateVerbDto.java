package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class UpdateVerbDto implements ProtocolDTO {

    private String name;
    private String toCaller;
    private String toLocation;
    private Long verbId;


    public UpdateVerbDto(Long verbId, String name, String toCaller, String toLocation){
        this.name = name;
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
                ", toCaller='" + toCaller + '\'' +
                ", toLocation='" + toLocation + '\'' +
                ", verbId=" + verbId +
                '}';
    }
}
