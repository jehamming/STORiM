package com.hamming.storim.factories;

import com.hamming.storim.Database;
import com.hamming.storim.model.Thing;
import com.hamming.storim.model.User;

import java.util.List;

public class ThingFactory  {
    private static ThingFactory instance;

    private ThingFactory() {
            };


    public static ThingFactory getInstance() {
        if ( instance == null ) {
            instance = new ThingFactory();
        }
        return instance;
    }

    public List<Thing> getThings() {
        return Database.getInstance().getAll(Thing.class);
    }

    public Thing findThingById( Long id ) {
        return Database.getInstance().findById(Thing.class, id);
    }

    public Thing createThing(String name, User creator) {
        Long id = Database.getInstance().getNextID();
        Thing thing = new Thing(id);
        thing.setName(name);
        thing.setCreator(creator);
        thing.setOwner(creator);
        Database.getInstance().addBasicObject(thing);
        return thing;
    }


}
