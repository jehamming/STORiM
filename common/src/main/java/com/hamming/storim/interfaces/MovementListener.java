package com.hamming.storim.interfaces;

import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.LocationDto;

public interface MovementListener {

    public void userMoved(UserDto user, LocationDto newUserLocation);

    public void currentUserMoved(Long sequence, UserDto user, LocationDto newUserLocation);

    public void teleported(UserDto user, LocationDto location, Long fromRoom);

}
