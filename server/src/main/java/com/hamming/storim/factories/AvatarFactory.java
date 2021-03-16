package com.hamming.storim.factories;

import com.hamming.storim.Database;
import com.hamming.storim.ImageStore;
import com.hamming.storim.model.Avatar;
import com.hamming.storim.model.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvatarFactory {
    private static AvatarFactory instance;

    private AvatarFactory() {
        readAllAvatars();
        sanityCheck();
    }

    private void sanityCheck() {
        List<Long> avatarIDs = getAllAvatarIds();
        for (Long id : avatarIDs) {
            Avatar avatar = findAvatarById(id);
            if (avatar == null) {
                System.out.println(getClass().getName() + ": sanityCheck(), Avatar  "+ id +"' has no Image, deleting Avatar");
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
        ImageStore.deleteImageFile(Avatar.class, avatar.getId());
        Database.getInstance().removeBasicObject(Avatar.class, avatar);
    }

    private void readAllAvatars() {
        Map<Long, Image> images = ImageStore.readAllImages(Avatar.class);
        for (Long id : images.keySet()) {
            Avatar avatar = findAvatarById(id);
            if (avatar != null ) {
                avatar.setImage(images.get(id));
            } else {
                System.out.println(getClass().getName() + ": sanityCheck, have Image for Avatar " + id +" , but Avatar not in Database.");
                ImageStore.deleteImageFile(Avatar.class, id);
            }
        }
    }



    public static AvatarFactory getInstance() {
        if ( instance == null ) {
            instance = new AvatarFactory();
        }
        return instance;
    }


    public Avatar findAvatarById(Long id ) {
        return Database.getInstance().findById(Avatar.class, id);
    }

    public Avatar createAvatar(User creator, String name, Image image) {
        Long id = Database.getInstance().getNextID();
        return createAvatar(id, creator, name, image);
    }

    private Avatar createAvatar(Long id, User creator, String name, Image image) {
        Avatar avatar = new Avatar(id);
        avatar.setImage(image);
        avatar.setCreator(creator);
        avatar.setOwner(creator);
        avatar.setName(name);
        ImageStore.storeImageObject(avatar);
        Database.getInstance().addBasicObject(avatar);
        return avatar;
    }

    public List<Avatar> getAllAvatars() {
        return Database.getInstance().getAll(Avatar.class);
    }


    public List<Avatar> getAvatars(User user) {
        List<Avatar> list = new ArrayList<>();
        for (Avatar t : getAllAvatars()) {
            if (t.getOwner().getId().equals( user.getId())) {
                list.add(t);
            }
        }
        return list;
    }
}
