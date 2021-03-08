package com.hamming.storim.game;

import com.hamming.storim.model.User;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private List<User> onlineUsers;

    public GameState() {
        onlineUsers = new ArrayList<User>();
    }

    public List<User> getOnlineUsers() {
        return onlineUsers;
    }

}
