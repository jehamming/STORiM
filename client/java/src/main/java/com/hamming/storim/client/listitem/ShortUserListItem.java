package com.hamming.storim.client.listitem;

public class ShortUserListItem {
    private Long userId;
    private String name;
    public ShortUserListItem(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }

    public Long getUserId() {
        return userId;
    }
}
