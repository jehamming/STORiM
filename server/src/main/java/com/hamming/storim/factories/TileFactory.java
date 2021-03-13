package com.hamming.storim.factories;

import com.hamming.storim.Database;
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
        for (Tile tile: getAllTiles()) {
            if (tile.getImage() == null) {
                System.out.println(getClass().getName() + ": sanityCheck(), Tile  "+ tile.getId() +"' has no Image, deleting Tile");
                deleteTile(tile);
            }
        }
    }

    private void deleteTile(Tile tile) {
        Database.getInstance().removeBasicObject(Tile.class, tile);
    }

    private void readAllTiles() {
        String dataDir = ServerConfig.getInstance().getDataDirectory();
        String tileDirectoryPath = dataDir.concat(File.separator).concat(TILE_DIR);
        File tileDir = new File(tileDirectoryPath);
        if ( tileDir.isDirectory() ) {
            for (File tileFile : tileDir.listFiles()) {
                readTileFile(tileFile);
            }
        }
    }

    private void readTileFile(File tileFile) {
        try {
            Image image = ImageIO.read(tileFile);
            Long id = Long.valueOf(tileFile.getName());
            Tile tile = findTileById(id);
            if (tile != null ) {
                tile.setImage(image);
            } else {
                System.out.println(getClass().getName() + ": Sanity check, tileFile  "+ tileFile.getName() +"' is not in the database, deleting file");
                tileFile.delete();
            }
            System.out.println(getClass().getName() + " - read "+ tileFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTileFile(Tile tile) {
        try {
            String dataDir = ServerConfig.getInstance().getDataDirectory();
            String tileDirectoryPath = dataDir.concat(File.separator).concat(TILE_DIR);
            File newFile = new File(tileDirectoryPath.concat(File.separator).concat(tile.getId().toString()));
            BufferedImage bufferedImage = ImageUtils.getBufferedImage(tile.getImage());
            ImageIO.write(bufferedImage, "jpg", newFile );
            System.out.println(getClass().getName() + " - wrote "+ newFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
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
        writeTileFile(tile);
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
