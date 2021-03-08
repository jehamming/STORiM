package com.hamming.storim.model.dto;

import com.hamming.storim.util.StringUtils;

public class ThingDto implements DTO {

    private String id;
    private String name;
    private String creatorID;
    private String ownerID;

    public ThingDto(String id, String name, String creatorID, String ownerID){
        this.id = id;
        this.name = name;
        this.creatorID = creatorID;
        this.ownerID = ownerID;
    }

    public String getId() {
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
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", creatorID='" + creatorID + '\'' +
                ", ownerID='" + ownerID + '\'' +
                '}';
    }
}
