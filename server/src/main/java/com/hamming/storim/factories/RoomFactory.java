package com.hamming.storim.factories;

import com.hamming.storim.Database;
import com.hamming.storim.model.Room;
import com.hamming.storim.model.User;

import java.util.List;

public class RoomFactory {
    private static RoomFactory instance;

    private RoomFactory() {
    };

    public static RoomFactory getInstance() {
        if ( instance == null ) {
            instance = new RoomFactory();
        }
        return instance;
    }

    public Room findRoomByID( Long id ) {
        return Database.getInstance().findById(Room.class, id);
    }


    public Room createRoom(User creator, String name, Integer size) {
        Long id = Database.getInstance().getNextID();
        Room room = new Room(id);
        room.setName(name);
        room.setCreator(creator);
        room.setOwner(creator);
        room.setSize(size);
        Database.getInstance().addBasicObject(room);
        return room;
    }


    public Room updateRoom(Long roomId, String name, Integer size) {
        Room room = findRoomByID(roomId);
        room.setName(name);
        room.setSize(size);
        return room;
    }

    public boolean deleteRoom(Long roomId) {
        boolean success = false;
        Room room = findRoomByID(roomId);
        if (room != null ) {
            Database.getInstance().removeBasicObject(Room.class, room);
            success = true;
        }
        return success;
    }
}
