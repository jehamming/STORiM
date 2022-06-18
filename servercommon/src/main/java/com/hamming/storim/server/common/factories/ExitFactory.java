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

    private void sanityCheck() {
        List<Long> exitIds = getAllExitIds();
        for (Long id : exitIds) {
            Exit exit = findExitById(id);
            if (exit == null) {
                Logger.info(this, getClass().getName() + ": sanityCheck(), Thing  "+ id +"' has no Image, deleting Thing");
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
                //FIXME Add images to exits!
                // exit.setImage(images.get(id));
            } else {
                Logger.info(this, getClass().getName() + ": sanityCheck, have Image for Exit " + id +" , but Exit not in Database.");
                ImageStore.deleteImageFile(Exit.class, id, dataDir);
            }
        }
    }




    public Exit findExitById(Long id ) {
        return Database.getInstance().findById(Exit.class, id);
    }


    public Exit createExit(Long creatorId, String name, Exit.Orientation orientation, Long toRoomId) {
        Long id = Database.getInstance().getNextID();
        Exit exit = new Exit(name, orientation, toRoomId);
        exit.setId(id);
        exit.setCreatorId(creatorId);
        exit.setOwnerId(creatorId);
        exit.setName(name);
        //FIXME Add images to exits!
        // ImageStore.storeImageObject(exit, dataDir);
        Database.getInstance().addBasicObject(exit);
        return exit;
    }

    public List<Exit> getAllExits() {
        return Database.getInstance().getAll(Exit.class);
    }

}
