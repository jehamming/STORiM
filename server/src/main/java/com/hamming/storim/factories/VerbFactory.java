package com.hamming.storim.factories;

import com.hamming.storim.Database;
import com.hamming.storim.model.Room;
import com.hamming.storim.model.Verb;
import com.hamming.storim.model.User;

import java.util.List;

public class VerbFactory {
    private static VerbFactory instance;

    private VerbFactory() { };


    public static VerbFactory getInstance() {
        if ( instance == null ) {
            instance = new VerbFactory();
        }
        return instance;
    }

    public List<Verb> getVerbs() {
        return Database.getInstance().getAll(Verb.class);
    }

    public Verb findVerbByID(Long id ) {
        return Database.getInstance().findById(Verb.class, id);
    }

    public Verb createVerb(String name, User creator) {
        Long id = Database.getInstance().getNextID();
        Verb verb = new Verb(id);
        verb.setName(name);
        verb.setCreator(creator);
        verb.setOwner(creator);
        Database.getInstance().addBasicObject(verb);
        return verb;
    }


    public Verb createVerb(User creator, String name, String shortName, String toCaller, String toLocation) {
        Long id = Database.getInstance().getNextID();
        Verb verb = new Verb(id);
        verb.setName(name);
        verb.setCreator(creator);
        verb.setOwner(creator);
        verb.setShortName(shortName);
        verb.setName(name);
        verb.setToCaller(toCaller);
        verb.setToLocation(toLocation);
        Database.getInstance().addBasicObject(verb);
        return verb;
    }

    public Verb updateVerb(Long verbId, String name, String shortName, String toCaller, String toLocation) {
        Verb verb = findVerbByID(verbId);
        if ( verb != null ) {
            verb.setName(name);
            verb.setShortName(shortName);
            verb.setName(name);
            verb.setToCaller(toCaller);
            verb.setToLocation(toLocation);
        }
        return verb;
    }

    public boolean deleteVerb(Long verbID) {
        boolean success = false;
        Verb verb = Database.getInstance().findById(Verb.class, verbID);
        if ( verb != null ) {
            Database.getInstance().removeBasicObject(Verb.class, verb);
            success = true;
        }
        return success;
    }
}