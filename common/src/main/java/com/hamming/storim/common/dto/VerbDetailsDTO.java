package com.hamming.storim.common.dto;

public class VerbDetailsDTO extends DTO {

    private String toCaller;
    private String toLocation;


    public VerbDetailsDTO(Long id, String name, String toCaller, String toLocation){
        setId(id);
        setName(name);
        this.toCaller = toCaller;
        this.toLocation = toLocation;
    }

    public String getToCaller() {
        return toCaller;
    }

    public String getToLocation() {
        return toLocation;
    }

}
