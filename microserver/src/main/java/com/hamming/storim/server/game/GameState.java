package com.hamming.storim.server.game;


import com.hamming.storim.server.common.model.User;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private List<User> onlineUsers;

    public GameState() {
        onlineUsers = new ArrayList<>();
    }

    public List<User> getOnlineUsers() {
        return onlineUsers;
    }

}
