package com.hamming.storim.server;

import com.hamming.storim.server.common.factories.ExitFactory;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Exit;
import com.hamming.storim.server.common.model.Room;

public class CreateMinimalMicroserverDataset {

    public void createMinimalWorld()  {
        //Make sure that there is a clean, empty database
        Database.getInstance(STORIMMicroServer.DBFILE).clearDatabase();

        Room mainSquare = RoomFactory.getInstance().createRoom(1l, "Main square");
        Room meadow = RoomFactory.getInstance().createRoom(1l, "A meadow");
        Room forest = RoomFactory.getInstance().createRoom(1l, "A forest");

        Exit main2Meadow = ExitFactory.getInstance(STORIMMicroServer.DATADIR).createExit(1L, "To meadow" , Exit.Orientation.EAST, meadow.getId());
        mainSquare.addExit(main2Meadow);

        Exit main2Meadow2 = ExitFactory.getInstance(STORIMMicroServer.DATADIR).createExit(1L, "To meadow" , Exit.Orientation.NORTH, meadow.getId());
        mainSquare.addExit(main2Meadow2);

        Exit main2Forest = ExitFactory.getInstance(STORIMMicroServer.DATADIR).createExit(1L, "To forest" , Exit.Orientation.EAST, forest.getId());
        mainSquare.addExit(main2Forest);

        Exit main2Forest2 = ExitFactory.getInstance(STORIMMicroServer.DATADIR).createExit(1L, "To forest" , Exit.Orientation.SOUTH, forest.getId());
        mainSquare.addExit(main2Forest2);

        Exit meadow2Main = ExitFactory.getInstance(STORIMMicroServer.DATADIR).createExit(1L, "To main square" , Exit.Orientation.WEST, mainSquare.getId());
        meadow.addExit(meadow2Main);

        Exit forest2Main = ExitFactory.getInstance(STORIMMicroServer.DATADIR).createExit(1L, "To main square" , Exit.Orientation.WEST, mainSquare.getId());
        forest.addExit(forest2Main);

        Database.getInstance().store();

    }


    public static void main(String[] args) {
        CreateMinimalMicroserverDataset w = new CreateMinimalMicroserverDataset();
        w.createMinimalWorld();
    }
}
