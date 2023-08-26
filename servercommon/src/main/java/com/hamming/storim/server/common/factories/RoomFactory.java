package com.hamming.storim.server.common.factories;

import com.hamming.storim.server.Database;
import com.hamming.storim.server.common.model.Room;

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

    public Room findRoomByID(Long id ) {
        return Database.getInstance().findById(Room.class, id);
    }
    public Room findRoomByName(String name ) {
        return Database.getInstance().findByName(Room.class, name);
    }

    public List<Room> getRooms() {
        return Database.getInstance().getAll(Room.class);
    }


    public Room createRoom(Long creatorId, String name, int rows, int cols) {
        Room room = createRoom(creatorId, name);
        room.setSize(rows, cols);
        Database.getInstance().addBasicObject(room);
        return room;
    }

    public Room createRoom(Long creatorId, String name) {
        Long id = Database.getInstance().getNextID();
        Room room = new Room();
        room.setId(id);
        room.setName(name);
        room.setCreatorId(creatorId);
        room.setOwnerId(creatorId);
        Database.getInstance().addBasicObject(room);
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
