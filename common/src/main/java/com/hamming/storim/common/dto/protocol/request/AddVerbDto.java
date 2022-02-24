package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class AddVerbDto implements ProtocolDTO {

    private String name;
    private String toCaller;
    private String toLocation;


    public AddVerbDto(String name,  String toCaller, String toLocation){
        this.name = name;
        this.toCaller = toCaller;
        this.toLocation = toLocation;
    }


    public String getName() {
        return name;

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
                ", toCaller='" + toCaller + '\'' +
                ", toLocation='" + toLocation + '\'' +
                '}';
    }
}
