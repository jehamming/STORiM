package com.hamming.storim.client.listitem;

import com.hamming.storim.common.dto.UserDto;

public class UserListItem {
    private UserDto user;
    public UserListItem(UserDto usr) {
        this.user = usr;
    }
    @Override
    public String toString() {
        return user.getName();
    }
    public UserDto getUser() {
        return user;
    }
}
