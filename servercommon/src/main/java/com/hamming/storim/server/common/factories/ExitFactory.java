package com.hamming.storim.server.common.factories;


import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.Database;
import com.hamming.storim.server.common.ImageStore;
import com.hamming.storim.server.common.model.Exit;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExitFactory {
    private static ExitFactory instance;
    private String dataDir;

    private ExitFactory(String dataDir) {
        this.dataDir = dataDir;
        readAllExits();
        sanityCheck();
    }

    public static ExitFactory getInstance(String dataDir) {
        if ( instance == null ) {
            instance = new ExitFactory(dataDir);
        }
        return instance;
    }

    public static ExitFactory getInstance() {
        return instance;
    }

    private void sanityCheck() {
        List<Long> exitIds = getAllExitIds();
        for (Long id : exitIds) {
            Exit exit = findExitById(id);
            if (exit == null) {
                Logger.info(this, getClass().getName() + ": sanityCheck(), Exit  "+ id +"' has no Image, deleting Exit");
                deleteExit(exit);
            }
        }
    }

    private List<Long> getAllExitIds() {
        List<Long> exitIds = new ArrayList<>();
        for (Exit exit : getAllExits() ) {
            exitIds.add(exit.getId());
        }
        return exitIds;
    }

    public void deleteExit(Exit exit) {
        ImageStore.deleteImageFile(Exit.class, exit.getId(), dataDir);
        Database.getInstance().removeBasicObject(Exit.class, exit);
    }

    private void readAllExits() {
        Map<Long, Image> images = ImageStore.readAllImages(Exit.class, dataDir);
        for (Long id : images.keySet()) {
            Exit exit = findExitById(id);
            if (exit != null ) {
                exit.setImage(images.get(id));
            } else {
                Logger.info(this, getClass().getName() + ": sanityCheck, have Image for Exit " + id +" , but Exit not in Database.");
                ImageStore.deleteImageFile(Exit.class, id, dataDir);
            }
        }
    }




    public Exit findExitById(Long id ) {
        return Database.getInstance().findById(Exit.class, id);
    }


    public Exit createExit(Long creatorId, String name, Long toRoomId, String toRoomURI, String description, Float scale, int rotation, Image image) {
        Long id = Database.getInstance().getNextID();
        Exit exit = new Exit(name, toRoomId, toRoomURI);
        exit.setId(id);
        exit.setCreatorId(creatorId);
        exit.setOwnerId(creatorId);
        exit.setName(name);
        exit.setToRoomID(toRoomId);
        exit.setToRoomURI(toRoomURI);
        exit.setDescription(description);
        exit.setScale(scale);
        exit.setRotation(rotation);
        exit.setImage(image);
        ImageStore.storeImageObject(exit, dataDir);
        Database.getInstance().addBasicObject(exit);
        return exit;
    }

    public void updateExit(Exit exit) {
        ImageStore.storeImageObject(exit, dataDir);
    }

    public List<Exit> getAllExits() {
        return Database.getInstance().getAll(Exit.class);
    }

}
