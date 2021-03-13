package com.hamming.storim.model.dto;

public class ThingDto implements DTO {

    private Long id;
    private String name;
    private String creatorID;
    private String ownerID;

    public ThingDto(Long id, String name, String creatorID, String ownerID){
        this.id = id;
        this.name = name;
        this.creatorID = creatorID;
        this.ownerID = ownerID;
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

    @Override
    public String toString() {
        return "ThingDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creatorID='" + creatorID + '\'' +
                ", ownerID='" + ownerID + '\'' +
                '}';
    }
}
