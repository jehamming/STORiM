package com.hamming.userdataserver.factories;


import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.Database;
import com.hamming.storim.server.common.ImageStore;
import com.hamming.userdataserver.model.Avatar;
import com.hamming.userdataserver.model.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvatarFactory {
    private static AvatarFactory instance;
    private String dataDir;

    private AvatarFactory(String dataDir) {
        this.dataDir = dataDir;
        readAllAvatars();
        sanityCheck();
    }

    public static AvatarFactory getInstance(String dataDir) {
        if ( instance == null ) {
            instance = new AvatarFactory(dataDir);
        }
        return instance;
    }

    public static AvatarFactory getInstance() {
        return instance;
    }

    private void sanityCheck() {
        List<Long> avatarIDs = getAllAvatarIds();
        for (Long id : avatarIDs) {
            Avatar avatar = findAvatarById(id);
            if (avatar == null) {
                Logger.info(this, getClass().getName() + ": sanityCheck(), Avatar  "+ id +"' has no Image, deleting Avatar");
                deleteAvatar(avatar);
            }
        }
    }

    private List<Long> getAllAvatarIds() {
        List<Long> avatarIDs = new ArrayList<>();
        for (Avatar avatar : getAllAvatars() ) {
            avatarIDs.add(avatar.getId());
        }
        return avatarIDs;
    }

    public void deleteAvatar(Avatar avatar) {
        ImageStore.deleteImageFile(Avatar.class, avatar.getId(), dataDir);
        Database.getInstance().removeBasicObject(Avatar.class, avatar);
    }

    private void readAllAvatars() {
        Map<Long, Image> images = ImageStore.readAllImages(Avatar.class, dataDir);
        for (Long id : images.keySet()) {
            Avatar avatar = findAvatarById(id);
            if (avatar != null ) {
                avatar.setImage(images.get(id));
            } else {
                Logger.info(this, getClass().getName() + ": sanityCheck, have Image for Avatar " + id +" , but Avatar not in Database.");
                ImageStore.deleteImageFile(Avatar.class, id, dataDir);
            }
        }
    }






    public Avatar findAvatarById(Long id ) {
        return Database.getInstance().findById(Avatar.class, id);
    }

    public Avatar createAvatar(Long creatorId, String name, Image image) {
        Long id = Database.getInstance().getNextID();
        return createAvatar(id, creatorId, name, image);
    }

    private Avatar createAvatar(Long id, Long creatorId, String name, Image image) {
        Avatar avatar = new Avatar();
        avatar.setId(id);
        avatar.setImage(image);
        avatar.setCreatorId(creatorId);
        avatar.setOwnerId(creatorId);
        avatar.setName(name);
        ImageStore.storeImageObject(avatar, dataDir);
        Database.getInstance().addBasicObject(avatar);
        return avatar;
    }

    public List<Avatar> getAllAvatars() {
        return Database.getInstance().getAll(Avatar.class);
    }


    public List<Avatar> getAvatars(User user) {
        List<Avatar> list = new ArrayList<>();
        for (Avatar t : getAllAvatars()) {
            if (t.getOwnerId().equals( user.getId())) {
                list.add(t);
            }
        }
        return list;
    }
}
