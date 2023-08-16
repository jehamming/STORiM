package com.hamming.storim.common.dto.protocol.serverpush;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class UserOnlineDTO extends ResponseDTO {

    private Long userId;
    private String name;

    public UserOnlineDTO(Long userId, String name) {
        super(true, null);
        this.userId = userId;
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "UserOnlineDTO{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                '}';
    }
}
