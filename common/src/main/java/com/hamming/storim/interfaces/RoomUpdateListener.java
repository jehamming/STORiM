package com.hamming.storim.interfaces;

import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.ThingDto;
import com.hamming.storim.model.dto.UserDto;

public interface RoomUpdateListener {
     void roomAdded(RoomDto room);
     void roomDeleted(RoomDto room);
     void roomUpdated(RoomDto room);
}
