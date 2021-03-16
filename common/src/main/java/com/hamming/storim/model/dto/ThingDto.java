package com.hamming.storim.model.dto;

public class ThingDto extends DTO {


    public ThingDto(Long id, String name, Long creatorID, Long ownerID){
        setId(id);
        setName(name);
        setCreatorID(creatorID);
        setOwnerID(ownerID);
    }


    @Override
    public String toString() {
        return "ThingDto{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", creatorID='" + getCreatorID() + '\'' +
                ", ownerID='" + getOwnerID() + '\'' +
                '}';
    }
}
