package com.hamming.storim.server;

import com.hamming.storim.server.common.factories.ExitFactory;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Exit;
import com.hamming.storim.server.common.model.Room;

public class CreateMinimalMicroserverDataset {

    public void createMinimalWorld()  {
        //Make sure that there is a clean, empty database
        Database.getInstance(STORIMMicroServer.DBFILE).clearDatabase();

        RoomFactory.getInstance().createRoom(1l, "Main square");
        RoomFactory.getInstance().createRoom(1l, "A meadow");
        RoomFactory.getInstance().createRoom(1l, "A forest");

        Database.getInstance().store();

    }


    public static void main(String[] args) {
        CreateMinimalMicroserverDataset w = new CreateMinimalMicroserverDataset();
        w.createMinimalWorld();
    }
}
