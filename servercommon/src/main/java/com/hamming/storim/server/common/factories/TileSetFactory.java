package com.hamming.storim.server.common.factories;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.Database;
import com.hamming.storim.server.common.ImageStore;
import com.hamming.storim.server.common.model.TileSet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TileSetFactory {
    private static TileSetFactory instance;
    private String dataDir;

    public static String DEFAULT_TILESET_NAME = "Default";


    private TileSetFactory(String dataDir) {
        this.dataDir = dataDir;
        readAllTileSets();
        sanityCheck();
    }


    public static TileSetFactory getInstance(String dataDir) {
        if (instance == null) {
            instance = new TileSetFactory(dataDir);
        }
        return instance;
    }

    public static TileSetFactory getInstance() {
        return instance;
    }


    private void sanityCheck() {
        List<Long> tileSetIds = getAllTileSetIds();
        for (Long id : tileSetIds) {
            TileSet tile = findTileSetById(id);
            if (tile.getImage() == null) {
                Logger.info(this, getClass().getName() + ": sanityCheck(), TileSet  "+ id +"' has no Image, deleting TileSet");
                deleteTileSet(tile);
            }
        }
    }

    private List<Long> getAllTileSetIds() {
        List<Long> tileSetIds = new ArrayList<>();
        for (TileSet tileSet : getAllTileSets() ) {
            tileSetIds.add(tileSet.getId());
        }
        return tileSetIds;
    }

    public void deleteTileSet(TileSet tileSet) {
        ImageStore.deleteImageFile(TileSet.class, tileSet.getId(), dataDir);
        Database.getInstance().removeBasicObject(TileSet.class, tileSet);
    }

    private void readAllTileSets() {
        Map<Long, Image> images = ImageStore.readAllImages(TileSet.class,dataDir);
        for (Long id : images.keySet()) {
            TileSet tileSet = findTileSetById(id);
            if (tileSet != null ) {
                tileSet.setImage(images.get(id));
            } else {
                Logger.info(this, getClass().getName() + ": sanityCheck, have Image for tileSet " + id +" , but TileSet not in Database.");
                ImageStore.deleteImageFile(TileSet.class, id, dataDir);
            }
        }

    }

    public TileSet findTileSetById( Long id ) {
        return Database.getInstance().findById(TileSet.class, id);
    }


    public TileSet findTileSetByName( String name ) {
        return Database.getInstance().findByName(TileSet.class, name);
    }


    public TileSet createTileSet(Long creatorId, String name, Image image, int tileWidth, int tileHeight) {
        Long id = Database.getInstance().getNextID();
        return createTileSet(id, name, creatorId, image, tileWidth, tileHeight);
    }

    private TileSet createTileSet(Long id, String name, Long creatorId, Image image, int tileWidth, int tileHeight) {
        TileSet tile = new TileSet(id, name, image, tileWidth, tileHeight);
        tile.setId(id);
        tile.setImage(image);
        tile.setCreatorId(creatorId);
        tile.setOwnerId(creatorId);
        ImageStore.storeImageObject(tile, dataDir);
        Database.getInstance().addBasicObject(tile);
        return tile;
    }

    public List<TileSet> getAllTileSets() {
        return Database.getInstance().getAll(TileSet.class);
    }



    public List<TileSet> geTileSetsForUser(Long userId) {
        List<TileSet> list = new ArrayList<>();
        for (TileSet t : getAllTileSets()) {
            if (t.getOwnerId().equals( userId)) {
                list.add(t);
            }
        }
        return list;
    }

}
