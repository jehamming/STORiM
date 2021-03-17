package com.hamming.storim.factories;

import com.hamming.storim.Database;
import com.hamming.storim.ImageStore;
import com.hamming.storim.model.Thing;
import com.hamming.storim.model.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThingFactory {
    private static ThingFactory instance;

    private ThingFactory() {
        readAllThings();
        sanityCheck();
    }

    private void sanityCheck() {
        List<Long> thingIDs = getAllThingIds();
        for (Long id : thingIDs) {
            Thing thing = findThingById(id);
            if (thing == null) {
                System.out.println(getClass().getName() + ": sanityCheck(), Thing  "+ id +"' has no Image, deleting Thing");
                deleteThing(thing);
            }
        }
    }

    private List<Long> getAllThingIds() {
        List<Long> thingIDs = new ArrayList<>();
        for (Thing thing : getAllThings() ) {
            thingIDs.add(thing.getId());
        }
        return thingIDs;
    }

    public void deleteThing(Thing thing) {
        ImageStore.deleteImageFile(Thing.class, thing.getId());
        Database.getInstance().removeBasicObject(Thing.class, thing);
    }

    private void readAllThings() {
        Map<Long, Image> images = ImageStore.readAllImages(Thing.class);
        for (Long id : images.keySet()) {
            Thing thing = findThingById(id);
            if (thing != null ) {
                thing.setImage(images.get(id));
            } else {
                System.out.println(getClass().getName() + ": sanityCheck, have Image for Avatar " + id +" , but Avatar not in Database.");
                ImageStore.deleteImageFile(Thing.class, id);
            }
        }
    }



    public static ThingFactory getInstance() {
        if ( instance == null ) {
            instance = new ThingFactory();
        }
        return instance;
    }


    public Thing findThingById(Long id ) {
        return Database.getInstance().findById(Thing.class, id);
    }


    public Thing createThing(User creator, String name, String description, Float scale, int rotation, Image image) {
        Long id = Database.getInstance().getNextID();
        Thing thing = new Thing(id);
        thing.setImage(image);
        thing.setCreator(creator);
        thing.setOwner(creator);
        thing.setName(name);
        thing.setDescription(description);
        thing.setScale(scale);
        thing.setRotation(rotation);
        ImageStore.storeImageObject(thing);
        Database.getInstance().addBasicObject(thing);
        return thing;
    }

    public List<Thing> getAllThings() {
        return Database.getInstance().getAll(Thing.class);
    }


    public List<Thing> getThings(User user) {
        List<Thing> list = new ArrayList<>();
        for (Thing t : getAllThings()) {
            if (t.getOwner().getId().equals( user.getId())) {
                list.add(t);
            }
        }
        return list;
    }
}
