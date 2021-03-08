package com.hamming.storim.model.dto;

public class VerbDto implements DTO {

    private Long id;
    private String name;
    private String creatorID;
    private String ownerID;
    private String shortName;
    private String toCaller;
    private String toLocation;


    public VerbDto(Long id, String creatorID, String ownerID, String name, String shortName, String toCaller, String toLocation){
        this.id = id;
        this.creatorID = creatorID;
        this.ownerID = ownerID;
        this.name = name;
        this.shortName = shortName;
        this.toCaller = toCaller;
        this.toLocation = toLocation;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public String getOwnerID() {
        return ownerID;
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
        return "CommandDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creatorID='" + creatorID + '\'' +
                ", ownerID='" + ownerID + '\'' +
                ", shortName='" + shortName + '\'' +
                ", toCaller='" + toCaller + '\'' +
                ", toLocation='" + toLocation + '\'' +
                '}';
    }
}
