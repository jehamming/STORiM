package com.hamming.storim.model.dto.protocol.verb;

import com.hamming.storim.model.dto.DTO;
import com.hamming.storim.model.dto.protocol.ProtocolDTO;

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
