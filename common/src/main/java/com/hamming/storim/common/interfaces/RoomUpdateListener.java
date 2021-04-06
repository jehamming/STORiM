package com.hamming.storim.common.interfaces;

import com.hamming.storim.common.dto.RoomDto;

public interface RoomUpdateListener {
     void roomAdded(RoomDto room);
     void roomDeleted(RoomDto room);
     void roomUpdated(RoomDto room);
}
