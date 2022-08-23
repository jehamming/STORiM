package com.hamming.storim.server.common.dto.protocol.dataserver.avatar;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class UpdateThingRequestDto extends ProtocolDTO {

    private Long thingId;
    private String name;
    private String description;
    private float scale;
    private int rotation;
    private byte[] imageData;

    public UpdateThingRequestDto(Long thingId, String name, String description, float scale, int rotation, byte[] imageData) {
        this.thingId = thingId;
        this.name = name;
        this.description = description;
        this.scale = scale;
        this.rotation = rotation;
        this.imageData = imageData;
    }

    public Long getThingId() {
        return thingId;
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
        return "UpdateThingRequestDto{" +
                "thingId=" + thingId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", scale=" + scale +
                ", rotation=" + rotation +
                '}';
    }
}
