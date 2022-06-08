package com.hamming.storim.server.game;


import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.server.common.model.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameState {

    private List<UserDto> onlineUsers;
    private HashMap<Long, Location> userLocations;
    private HashMap<Long, Location> thingLocations;

    public GameState() {
        onlineUsers = new ArrayList<>();
        userLocations = new HashMap<>();
        thingLocations = new HashMap<>();
    }

    public List<UserDto> getOnlineUsers() {
        return onlineUsers;
    }

    public boolean removeOnlineUser(UserDto user) {
        boolean existed = onlineUsers.remove(user);
        userLocations.remove(user.getId());
        return existed;
    }

    public Location getUserLocation(Long userId) {
        return userLocations.get(userId);
    }

    public Location getThingLocation(Long thingId) {
        return userLocations.get(thingId);
    }


    public void setUserLocation(UserDto user, Location location) {
        userLocations.put(user.getId(), location);
    }

    public void setThingLocation(ThingDto thing, Location location) {
        userLocations.put(thing.getId(), location);
    }

    public UserDto findUserById( Long id ) {
        UserDto returnValue = null;
        for (UserDto u : onlineUsers ) {
            if (u.getId().equals(id)) {
                returnValue = u;
                break;
            }
        }
        return returnValue;
    }

    public void addOnlineUser(UserDto user) {
        onlineUsers.add(user);
    }
}
