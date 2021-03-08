package com.hamming.storim.interfaces;

import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.UserDto;

public interface RoomListener {

     void userInRoom(UserDto user, RoomDto room, LocationDto location);
     void userTeleportedInRoom(UserDto user, RoomDto room);
     void userLeftRoom(UserDto user, RoomDto room);

     void roomAdded(RoomDto room);
     void roomDeleted(RoomDto room);

}
