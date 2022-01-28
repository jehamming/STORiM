package com.hamming.storim.server.game;


import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.common.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameState {

    private List<User> onlineUsers;
    private HashMap<Long, Location> userLocations;

    public GameState() {
        onlineUsers = new ArrayList<>();
        userLocations = new HashMap<>();
    }

    public List<User> getOnlineUsers() {
        return onlineUsers;
    }

    public boolean removeOnlineUser(User user) {
        boolean existed = onlineUsers.remove(user);
        userLocations.remove(user.getId());
        return existed;
    }

    public Location getLocation(Long userId) {
        return userLocations.get(userId);
    }

    public void setLocation(User user, Location location) {
        userLocations.put(user.getId(), location);
    }

    public User findUserById( Long id ) {
        User returnValue = null;
        for (User u : onlineUsers ) {
            if (u.getId().equals(id)) {
                returnValue = u;
                break;
            }
        }
        return returnValue;
    }

    public void addOnlineUser(User user) {
        onlineUsers.add(user);
    }
}
