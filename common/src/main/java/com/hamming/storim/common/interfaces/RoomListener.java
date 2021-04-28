package com.hamming.storim.common.interfaces;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.UserDto;

public interface RoomListener {

    void userInRoom(UserDto user, LocationDto location);

    void userEnteredRoom(UserDto user, LocationDto location);

    void userLeftRoom(UserDto user);

    void userLocationUpdate(UserDto user, LocationDto location);

    void currentUserLocationUpdate(Long sequenceNumber, LocationDto location);

    void setRoom(RoomDto room, LocationDto location);

    void thingPlacedInRoom(ThingDto thing, UserDto byUser);

    void thingRemovedFromRoom(ThingDto thing);

    void thingInRoom(ThingDto thing);
}
