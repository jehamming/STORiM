package com.hamming.storim.server.common.dto.protocol.dataserver.verb;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class UpdateVerbRequestDto implements ProtocolDTO {

    private Long verbId;
    private String name;
    private String toCaller;
    private String toLocation;


    public UpdateVerbRequestDto(Long verbId, String name, String toCaller, String toLocation){
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
        return "UpdateVerbRequestDto{" +
                "verbId=" + verbId +
                ", name='" + name + '\'' +
                ", toCaller='" + toCaller + '\'' +
                ", toLocation='" + toLocation + '\'' +
                '}';
    }
}
