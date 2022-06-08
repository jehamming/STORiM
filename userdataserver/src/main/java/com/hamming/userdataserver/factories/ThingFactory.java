package com.hamming.userdataserver.factories;


import com.hamming.storim.server.Database;
import com.hamming.storim.server.common.ImageStore;
import com.hamming.userdataserver.model.Thing;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThingFactory {
    private static ThingFactory instance;
    private String dataDir;

    private ThingFactory(String dataDir) {
        this.dataDir = dataDir;
        readAllThings();
        sanityCheck();
    }

    public static ThingFactory getInstance(String dataDir) {
        if ( instance == null ) {
            instance = new ThingFactory(dataDir);
        }
        return instance;
    }

    public static ThingFactory getInstance() {
        return instance;
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
        ImageStore.deleteImageFile(Thing.class, thing.getId(), dataDir);
        Database.getInstance().removeBasicObject(Thing.class, thing);
    }

    public boolean deleteThing(Long thingId) {
        boolean success = false;
        Thing thing = findThingById(thingId);
        if ( thing != null ) {
            deleteThing(thing);
            success = true;
        }
        return success;
    }

    private void readAllThings() {
        Map<Long, Image> images = ImageStore.readAllImages(Thing.class, dataDir);
        for (Long id : images.keySet()) {
            Thing thing = findThingById(id);
            if (thing != null ) {
                thing.setImage(images.get(id));
            } else {
                System.out.println(getClass().getName() + ": sanityCheck, have Image for Avatar " + id +" , but Avatar not in Database.");
                ImageStore.deleteImageFile(Thing.class, id, dataDir);
            }
        }
    }




    public Thing findThingById(Long id ) {
        return Database.getInstance().findById(Thing.class, id);
    }


    public Thing createThing(Long creatorId, String name, String description, Float scale, int rotation, Image image) {
        Long id = Database.getInstance().getNextID();
        Thing thing = new Thing();
        thing.setId(id);
        thing.setImage(image);
        thing.setCreatorId(creatorId);
        thing.setOwnerId(creatorId);
        thing.setName(name);
        thing.setDescription(description);
        thing.setScale(scale);
        thing.setRotation(rotation);
        ImageStore.storeImageObject(thing, dataDir);
        Database.getInstance().addBasicObject(thing);
        return thing;
    }

    public List<Thing> getAllThings() {
        return Database.getInstance().getAll(Thing.class);
    }

    public List<Thing> getThings(Long userId) {
        return Database.getInstance().getAll(Thing.class, userId);
    }
}
