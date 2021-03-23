package com.hamming.test;

import com.hamming.storim.Database;
import com.hamming.storim.factories.*;
import com.hamming.storim.model.*;

public class CreateMinimalWorld {

    public void createMinimalWorld()  {
        //Make sure that there is a clean, empty database
        Database.getInstance().clearDatabase();

        // Root User
        User rootUser = UserFactory.getInstance().addUser(null, "Root", "root", "root", "root@hamming.com");


        // Luuk
        User userLuuk = UserFactory.getInstance().addUser( rootUser, "Luuk Hamming", "lhhamming", "lhhamming", "luuk.hamming@gmail.com");
        Verb cmdSayLuuk = VerbFactory.getInstance().createVerb("Say", userLuuk);
        cmdSayLuuk.setToCaller("You say '${message}'");
        cmdSayLuuk.setToLocation("${caller} says '${message}'");
        Room roomLuuk = RoomFactory.getInstance().createRoom(userLuuk, "Luuks Room", 20);
        Location l1 = new Location(roomLuuk, roomLuuk.getSpawnPointX(), roomLuuk.getSpawnPointY());
        userLuuk.setLocation(l1);

        // Jan-Egbert
        User userJan = UserFactory.getInstance().addUser(rootUser,"Jan-Egbert Hamming", "jehamming", "jehamming", "janneman@hotmail.com");
        Verb cmdSayJan = VerbFactory.getInstance().createVerb("Say", userJan);
        cmdSayJan.setToCaller("You say '${message}'");
        cmdSayJan.setToLocation("${caller} says '${message}'");
        Room roomJan = RoomFactory.getInstance().createRoom(userJan, "Jan's Room", 20);
        Location l2 = new Location(roomJan, roomJan.getSpawnPointX(), roomJan.getSpawnPointY());
        userJan.setLocation(l2);

        Database.getInstance().store();

    }


    public static void main(String[] args) {
        CreateMinimalWorld w = new CreateMinimalWorld();
        w.createMinimalWorld();
    }
}
