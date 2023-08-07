package com.hamming.userdataserver;

import com.hamming.storim.server.Database;
import com.hamming.userdataserver.factories.VerbFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.User;
import com.hamming.userdataserver.model.Verb;

public class CreateMinimalUserDataSet {

    public void createMinimalUserDataSet()  {
        //Make sure that there is a clean, empty database
        Database.getInstance(STORIMUserDataServer.DBFILE).clearDatabase();

        // Root User
        User rootUser = UserFactory.getInstance().addUser(null, "Root", "root", "root", "root@hamming.com");


        // Player1
        User playerOne = UserFactory.getInstance().addUser( rootUser, "Player One", "player1", "player1", "player1@nomail.com");
        Verb cmdSayPlayer1 = VerbFactory.getInstance().createVerb("Say", playerOne);
        cmdSayPlayer1.setToCaller("You say '${message}'");
        cmdSayPlayer1.setToLocation("${caller} says '${message}'");

        // Player2
        User player2 = UserFactory.getInstance().addUser(rootUser,"Player Two", "player2", "player2", "player2@nomail.com");
        Verb cmdSayPlayer2 = VerbFactory.getInstance().createVerb("Say", player2);
        cmdSayPlayer2.setToCaller("You say '${message}'");
        cmdSayPlayer2.setToLocation("${caller} says '${message}'");

        Database.getInstance().store();

    }


    public static void main(String[] args) {
        CreateMinimalUserDataSet w = new CreateMinimalUserDataSet();
        w.createMinimalUserDataSet();
    }
}
