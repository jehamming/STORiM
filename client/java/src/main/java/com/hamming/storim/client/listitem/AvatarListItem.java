package com.hamming.storim.client.listitem;

import com.hamming.storim.common.dto.AvatarDto;

public class AvatarListItem {
    private AvatarDto avatar;

    public AvatarListItem(AvatarDto avatar) {
        this.avatar = avatar;
    }

    public AvatarDto getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarDto avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return avatar.getName();
    }
}
