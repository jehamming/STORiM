package com.hamming.storim.model.dto;

public class RoomDto implements DTO {

    private Long id;
    private String name;
    private Long creatorID;
    private Long ownerID;
    private Long tileID;
    private int size;

    public RoomDto(Long id, String name, Long creatorID, Long ownerID, int size, Long tileId){
        this.id = id;
        this.name = name;
        this.creatorID = creatorID;
        this.ownerID = ownerID;
        this.size = size;
        this.tileID = tileId;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCreatorID() {
        return creatorID;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public int getSize() {
        return size;
    }

    public Long getTileID() {
        return tileID;
    }

    @Override
    public String toString() {
        return "RoomDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creatorID=" + creatorID +
                ", ownerID=" + ownerID +
                ", tileID=" + tileID +
                ", size=" + size +
                '}';
    }
}
