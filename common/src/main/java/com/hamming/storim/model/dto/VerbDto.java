package com.hamming.storim.model.dto;

public class VerbDto extends DTO {

    private String toCaller;
    private String toLocation;


    public VerbDto(Long id, String name, String toCaller, String toLocation){
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
