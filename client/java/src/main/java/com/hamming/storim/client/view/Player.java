package com.hamming.storim.client.view;

public class Player extends BasicDrawableObject {

    private String displayName;

    public Player(Long userId) {
        setId(userId);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
