package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class MessageInRoomDTO extends ResponseDTO {

    public enum Type {USER, THING};

    private Long sourceID;
    private Type sourceType;
    private String message;

    public MessageInRoomDTO(Long sourceID, Type sourceType, String message) {
        this.sourceID = sourceID;
        this.sourceType = sourceType;
        this.message = message;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public Type getSourceType() {
        return sourceType;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageInRoomDTO{" +
                "sourceID=" + sourceID +
                ", sourceType=" + sourceType +
                ", message='" + message + '\'' +
                '}';
    }
}
