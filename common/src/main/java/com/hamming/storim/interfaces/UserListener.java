package com.hamming.storim.interfaces;

import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.UserDto;

public interface UserListener {
     void userConnected(UserDto user);
     void userDisconnected(UserDto user);
     void userOnline(UserDto user);
     void loginResult(boolean success, String message);

     void currentUserLocation(LocationDto loc);
     void userLocationUpdate(Long userId, LocationDto loc);
}
