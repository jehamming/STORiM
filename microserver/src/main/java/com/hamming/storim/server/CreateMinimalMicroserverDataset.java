package com.hamming.storim.server;

import com.hamming.storim.server.common.factories.RoomFactory;

public class CreateMinimalMicroserverDataset {

    public void createMinimalWorld()  {
        //Make sure that there is a clean, empty database
        Database.getInstance(STORIMMicroServer.DBFILE).clearDatabase();

        RoomFactory.getInstance().createRoom(1l, "Root Room");

        Database.getInstance().store();

    }


    public static void main(String[] args) {
        CreateMinimalMicroserverDataset w = new CreateMinimalMicroserverDataset();
        w.createMinimalWorld();
    }
}
