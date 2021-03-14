package com.hamming.storim.factories;

import com.hamming.storim.Database;
import com.hamming.storim.ImageStore;
import com.hamming.storim.ServerConfig;
import com.hamming.storim.model.Tile;
import com.hamming.storim.model.User;
import com.hamming.storim.util.ImageUtils;

import javax.imageio.ImageIO;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class TileFactory {
    private static TileFactory instance;
    private static String TILE_DIR = "tiles";

    private TileFactory() {
        readAllTiles();
        sanityCheck();
    }

    private void sanityCheck() {
        List<Long> tileIDs = getAllTileIds();
        for (Long id : tileIDs) {
            Tile tile = findTileById(id);
            if (tile == null) {
                System.out.println(getClass().getName() + ": sanityCheck(), Tile  "+ id +"' has no Image, deleting Tile");
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
        ImageStore.deleteImageFile(Tile.class, tile.getId());
        Database.getInstance().removeBasicObject(Tile.class, tile);
    }

    private void readAllTiles() {
        Map<Long, Image> images = ImageStore.readAllImages(Tile.class);
        for (Long id : images.keySet()) {
            Tile tile = findTileById(id);
            if (tile != null ) {
                tile.setImage(images.get(id));
            } else {
                System.out.println(getClass().getName() + ": sanityCheck, have Image for tile " + id +" , but Tile not in Database.");
                ImageStore.deleteImageFile(Tile.class, id);
            }
        }
    }



    public static TileFactory getInstance() {
        if ( instance == null ) {
            instance = new TileFactory();
        }
        return instance;
    }


    public Tile findTileById( Long id ) {
        return Database.getInstance().findById(Tile.class, id);
    }

    public Tile createTile(User creator, Image image) {
        Long id = Database.getInstance().getNextID();
        return createTile(id, creator, image);
    }

    private Tile createTile(Long id, User creator, Image image) {
        Tile tile = new Tile(id);
        tile.setImage(image);
        tile.setCreator(creator);
        tile.setOwner(creator);
        ImageStore.storeImageObject(tile);
        Database.getInstance().addBasicObject(tile);
        return tile;
    }

    public List<Tile> getAllTiles() {
        return Database.getInstance().getAll(Tile.class);
    }


    public List<Tile> geTiles(User user) {
        List<Tile> list = new ArrayList<>();
        for (Tile t : getAllTiles()) {
            if (t.getOwner().getId().equals( user.getId())) {
                list.add(t);
            }
        }
        return list;
    }
}
