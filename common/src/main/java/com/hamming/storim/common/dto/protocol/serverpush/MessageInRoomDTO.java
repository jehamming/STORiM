package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class MessageInRoomDTO extends ResponseDTO {

    public enum sType {USER, THING};
    public enum mType {VERB, MOVE, UPDATE};

    private Long sourceID;
    private sType sourceType;
    private String message;
    private mType messageType;

    public MessageInRoomDTO(Long sourceID, sType sourceType, String message, mType messageType) {
        this.sourceID = sourceID;
        this.sourceType = sourceType;
        this.message = message;
        this.messageType = messageType;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public String getMessage() {
        return message;
    }

    public sType getSourceType() {
        return sourceType;
    }

    public mType getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        return "MessageInRoomDTO{" +
                "sourceID=" + sourceID +
                ", sourceType=" + sourceType +
                ", message='" + message + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
