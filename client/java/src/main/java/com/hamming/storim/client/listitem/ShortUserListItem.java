package com.hamming.storim.client.listitem;

import com.hamming.storim.common.dto.UserDto;

public class ShortUserListItem {
    private Long userId;
    private String userName;
    public ShortUserListItem(Long userId, String username) {
        this.userId = userId;
        this.userName = username;
    }
    @Override
    public String toString() {
        return userName;
    }

    public Long getUserId() {
        return userId;
    }
}
