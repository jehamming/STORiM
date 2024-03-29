package com.hamming.storim.common.dto.protocol.request;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class AddExitDto extends ProtocolDTO {

    private String name;
    private String description;
    private float scale;
    private int rotation;
    private byte[] imageData;
    private Long toRoomID;
    private String roomURI;

    public AddExitDto(String name, String roomURI, Long toRoomID, String description, float scale, int rotation, byte[] imageData){
        this.name = name;
        this.imageData = imageData;
        this.description = description;
        this.scale = scale;
        this.rotation = rotation;
        this.toRoomID = toRoomID;
        this.roomURI = roomURI;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getScale() {
        return scale;
    }

    public int getRotation() {
        return rotation;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public Long getToRoomID() {
        return toRoomID;
    }

    public String getRoomURI() {
        return roomURI;
    }

    @Override
    public String toString() {
        return "AddExitDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", scale=" + scale +
                ", rotation=" + rotation +
                ", toRoomID=" + toRoomID +
                ", roomURI='" + roomURI + '\'' +
                '}';
    }
}
