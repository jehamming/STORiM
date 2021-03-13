package com.hamming.test;

import com.hamming.storim.Database;
import com.hamming.storim.factories.*;
import com.hamming.storim.model.*;

public class CreateMinimalWorld {

    public void createMinimalWorld()  {
        //Make sure that there is a clean, empty database
        Database.getInstance().clearDatabase();

        // Root User
        User rootUser = UserFactory.getInstance().addUser("Root", "root", "root", "root@hamming.com");

        // Commands
        Verb cmdSay = VerbFactory.getInstance().createVerb("Say", rootUser);
        cmdSay.setShortName("say");
        cmdSay.setToCaller("You say '${message}'");
        cmdSay.setToLocation("${caller} says '${message}'");

        Verb cmdShout = VerbFactory.getInstance().createVerb("Shout", rootUser);
        cmdShout.setShortName("shout");
        cmdShout.setToCaller("You shout '${message}'!!");
        cmdShout.setToLocation("${caller} shouts '${message}'!!");

        Verb cmdWhisper = VerbFactory.getInstance().createVerb("Whisper", rootUser);
        cmdWhisper.setShortName("whispers");
        cmdWhisper.setToCaller("You whisper '...${message}...'");
        cmdWhisper.setToLocation("${caller} whispers '...${message}...'");

        Verb cmdScream = VerbFactory.getInstance().createVerb("Scream", rootUser);
        cmdScream.setShortName("scream");
        cmdScream.setToCaller("You scream on the top of your lungs '...${message}...'");
        cmdScream.setToLocation("${caller} screams from the top of their lungs:  '${message}'!!!!");


        // Luuk
        User u1 = UserFactory.getInstance().addUser("Luuk Hamming", "lhhamming", "lhhamming", "luuk.hamming@gmail.com");
        u1.addVerb(cmdSay);
        u1.addVerb(cmdWhisper);
        u1.addVerb(cmdScream);
        Room roomLuuk = RoomFactory.getInstance().createRoom(u1, "Luuks Room", 20);
        u1.addRoom(roomLuuk);
        Location l1 = new Location(roomLuuk, roomLuuk.getSpawnPointX(), roomLuuk.getSpawnPointY());
        u1.setLocation(l1);
        Thing thingLuuk = ThingFactory.getInstance().createThing("Luuk's first thing!", u1);
        u1.addToInventory(thingLuuk);


        // Jan-Egbert
        User u2 = UserFactory.getInstance().addUser("Jan-Egbert Hamming", "jehamming", "jehamming", "janneman@hotmail.com");
        u2.addVerb(cmdSay);
        u2.addVerb(cmdShout);
        Room roomJan = RoomFactory.getInstance().createRoom(u2, "Jan's Room", 20);
        u2.addRoom(roomJan);
        Location l2 = new Location(roomJan, roomJan.getSpawnPointX(), roomJan.getSpawnPointY());
        u2.setLocation(l2);
        Thing thingJan = ThingFactory.getInstance().createThing("Jan's first thing!", u2);
        u2.addToInventory(thingJan);


        Database.getInstance().store();

    }


    public static void main(String[] args) {
        CreateMinimalWorld w = new CreateMinimalWorld();
        w.createMinimalWorld();
    }
}
