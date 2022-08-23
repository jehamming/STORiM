package com.hamming.storim.server.common.dto.protocol.dataserver.tile;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class AddThingRequestDto extends ProtocolDTO {

    private Long creatorId;
    private String name;
    private String description;
    private float scale;
    private int rotation;
    private byte[] imageData;

    public AddThingRequestDto(Long creatorId, String name, String description, float scale, int rotation, byte[] imageData) {
        this.creatorId = creatorId;
        this.name = name;
        this.description = description;
        this.scale = scale;
        this.rotation = rotation;
        this.imageData = imageData;
    }

    public Long getCreatorId() {
        return creatorId;
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

    @Override
    public String toString() {
        return "AddThingRequestDto{" +
                "creatorId=" + creatorId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", scale=" + scale +
                ", rotation=" + rotation +
                '}';
    }
}
