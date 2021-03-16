package com.hamming.storim.model.dto;

public class RoomDto extends DTO {

    private Long tileID;
    private int size;

    public RoomDto(Long id, String name, int size, Long tileId){
        setId(id);
        setName(name);
        this.size = size;
        this.tileID = tileId;
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
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", creatorID=" + getCreatorID() +
                ", ownerID=" + getOwnerID() +
                ", tileID=" + tileID +
                ", size=" + size +
                '}';
    }
}
