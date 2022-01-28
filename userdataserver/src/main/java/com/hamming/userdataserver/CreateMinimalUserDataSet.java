package com.hamming.userdataserver;

import com.hamming.storim.server.Database;
import com.hamming.storim.server.common.factories.VerbFactory;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.common.model.Verb;

public class CreateMinimalUserDataSet {

    public void createMinimalUserDataSet()  {
        //Make sure that there is a clean, empty database
        Database.getInstance(STORIMUserDataServer.DBFILE).clearDatabase();

        // Root User
        User rootUser = UserFactory.getInstance().addUser(null, "Root", "root", "root", "root@hamming.com");


        // Luuk
        User userLuuk = UserFactory.getInstance().addUser( rootUser, "Luuk Hamming", "lhhamming", "lhhamming", "luuk.hamming@gmail.com");
        Verb cmdSayLuuk = VerbFactory.getInstance().createVerb("Say", userLuuk);
        cmdSayLuuk.setToCaller("You say '${message}'");
        cmdSayLuuk.setToLocation("${caller} says '${message}'");

        // Jan-Egbert
        User userJan = UserFactory.getInstance().addUser(rootUser,"Jan-Egbert Hamming", "jehamming", "jehamming", "janneman@hotmail.com");
        Verb cmdSayJan = VerbFactory.getInstance().createVerb("Say", userJan);
        cmdSayJan.setToCaller("You say '${message}'");
        cmdSayJan.setToLocation("${caller} says '${message}'");


        Database.getInstance().store();

    }


    public static void main(String[] args) {
        CreateMinimalUserDataSet w = new CreateMinimalUserDataSet();
        w.createMinimalUserDataSet();
    }
}
