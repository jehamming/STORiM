package com.hamming.storim.client.view;

public class Player extends BasicDrawableObject {

    private String displayName;
    private boolean talking = false;

    public Player(Long userId) {
        setId(userId);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isTalking() {
        return talking;
    }

    public void setTalking(boolean talking) {
        this.talking = talking;
    }
}
