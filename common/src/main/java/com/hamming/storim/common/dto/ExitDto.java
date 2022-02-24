package com.hamming.storim.common.dto;

public class ExitDto extends DTO {

    public static enum Orientation { NORTH, SOUTH, EAST, WEST };

    private Orientation orientation;
    private Long roomid;

    public ExitDto(Long id, String name, Long roomId, Orientation orientation ){
        setId(id);
        setName(name);
        this.roomid = roomId;
        this.orientation = orientation;

    }

    public Orientation getOrientation() {
        return orientation;
    }

    public Long getRoomid() {
        return roomid;
    }

    @Override
    public String toString() {
        return "ExitDTO{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", roomId=" + roomid  +
                ", orientation=" + orientation +
                '}';
    }
}
