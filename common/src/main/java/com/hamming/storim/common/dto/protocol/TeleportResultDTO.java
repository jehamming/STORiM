package com.hamming.storim.common.dto.protocol;

import com.hamming.storim.common.dto.LocationDto;

public class TeleportResultDTO extends ProtocolResponseDTO {


    private boolean teleportSuccesful;
    private String errorMessage;
    private LocationDto location;
    private Long fromRoomID;

    public TeleportResultDTO( boolean teleportSuccesful, String errorMessage, LocationDto location, Long fromRoomID) {
        this.teleportSuccesful = teleportSuccesful;
        this. errorMessage = errorMessage;
        this.location = location;
        this.fromRoomID = fromRoomID;
    }

    public boolean isTeleportSuccesful() {
        return teleportSuccesful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LocationDto getLocation() {
        return location;
    }

    public Long getFromRoomID() {
        return fromRoomID;
    }

    @Override
    public String toString() {
        return "TeleportResultDTO{" +
                "teleportSuccesful=" + teleportSuccesful +
                ", errorMessage='" + errorMessage + '\'' +
                ", location=" + location +
                ", fromRoomID=" + fromRoomID +
                '}';
    }
}
