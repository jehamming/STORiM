package com.hamming.storim.interfaces;

import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.UserDto;

public interface RoomListener {

     void userInRoom(UserDto user, LocationDto location);
     void userEnteredRoom(UserDto user, LocationDto location);
     void userLeftRoom(UserDto user);
     void userLocationUpdate(UserDto user, LocationDto location);
     void currentUserLocationUpdate(Long sequenceNumber, LocationDto location);
     void setRoom(RoomDto room, LocationDto location);



}
