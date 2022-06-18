package com.hamming.userdataserver.factories;

import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.Database;
import com.hamming.storim.server.common.ImageStore;
import com.hamming.userdataserver.model.Tile;
import com.hamming.userdataserver.model.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TileFactory {
    private static TileFactory instance;
    private String dataDir;

    private TileFactory(String dataDir) {
        this.dataDir = dataDir;
        readAllTiles();
        sanityCheck();
    }


    public static TileFactory getInstance(String dataDir) {
        if ( instance == null ) {
            instance = new TileFactory(dataDir);
        }
        return instance;
    }

    public static TileFactory getInstance() {
        return instance;
    }


    private void sanityCheck() {
        List<Long> tileIDs = getAllTileIds();
        for (Long id : tileIDs) {
            Tile tile = findTileById(id);
            if (tile == null) {
                Logger.info(this, getClass().getName() + ": sanityCheck(), Tile  "+ id +"' has no Image, deleting Tile");
                deleteTile(tile);
            }
        }
    }

    private List<Long> getAllTileIds() {
        List<Long> tileIDs = new ArrayList<>();
        for (Tile tile : getAllTiles() ) {
            tileIDs.add(tile.getId());
        }
        return tileIDs;
    }

    private void deleteTile(Tile tile) {
        ImageStore.deleteImageFile(Tile.class, tile.getId(), dataDir);
        Database.getInstance().removeBasicObject(Tile.class, tile);
    }

    private void readAllTiles() {
        Map<Long, Image> images = ImageStore.readAllImages(Tile.class,dataDir);
        for (Long id : images.keySet()) {
            Tile tile = findTileById(id);
            if (tile != null ) {
                tile.setImage(images.get(id));
            } else {
                Logger.info(this, getClass().getName() + ": sanityCheck, have Image for tile " + id +" , but Tile not in Database.");
                ImageStore.deleteImageFile(Tile.class, id, dataDir);
            }
        }
    }

    public Tile findTileById( Long id ) {
        return Database.getInstance().findById(Tile.class, id);
    }

    public Tile createTile(Long creatorId, Image image) {
        Long id = Database.getInstance().getNextID();
        return createTile(id, creatorId, image);
    }

    private Tile createTile(Long id, Long creatorId, Image image) {
        Tile tile = new Tile();
        tile.setId(id);
        tile.setImage(image);
        tile.setCreatorId(creatorId);
        tile.setOwnerId(creatorId);
        ImageStore.storeImageObject(tile, dataDir);
        Database.getInstance().addBasicObject(tile);
        return tile;
    }

    public List<Tile> getAllTiles() {
        return Database.getInstance().getAll(Tile.class);
    }


    public List<Tile> geTiles(User user) {
        List<Tile> list = new ArrayList<>();
        for (Tile t : getAllTiles()) {
            if (t.getOwnerId().equals( user.getId())) {
                list.add(t);
            }
        }
        return list;
    }
}
