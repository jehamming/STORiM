package com.hamming.storim.server.common.dto.protocol.dataserver.verb;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class AddVerbRequestDto implements ProtocolDTO {

    private Long userId;
    private String name;
    private String toCaller;
    private String toLocation;


    public AddVerbRequestDto(Long userId, String name, String toCaller, String toLocation){
        this.name = name;
        this.toCaller = toCaller;
        this.toLocation = toLocation;
        this.userId = userId;
    }


    public String getName() {
        return name;

    }

    public Long getUserId() {
        return userId;
    }

    public String getToCaller() {
        return toCaller;
    }

    public String getToLocation() {
        return toLocation;
    }


    @Override
    public String toString() {
        return "AddVerbRequestDto{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", toCaller='" + toCaller + '\'' +
                ", toLocation='" + toLocation + '\'' +
                '}';
    }
}
