package com.hamming.storim.server.common.factories;

import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.server.Database;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.model.TileSet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileSetFactory {
    private static TileSetFactory instance;
    private String dataDir;
    private List<TileSet> tileSets;


    private TileSetFactory(String dataDir) {
        this.dataDir = dataDir;
        tileSets = new ArrayList<>();
        readAllTileSets();
        //   sanityCheck();
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


//    private void sanityCheck() {
//        List<Long> tileIDs = getAllTileIds();
//        for (Long id : tileIDs) {
//            Tile tile = findTileById(id);
//            if (tile.getImage() == null) {
//                Logger.info(this, getClass().getName() + ": sanityCheck(), Tile  "+ id +"' has no Image, deleting Tile");
//                deleteTile(tile);
//            } else {
//                if (UserFactory.getInstance().findUserById(tile.getOwnerId()) == null ) {
//                    Logger.info(this, getClass().getName() + ": sanityCheck(), Tile  "+ id +"' owner does not exist, deleting");
//                    deleteTile(tile);
//                }
//            }
//        }
//    }

//    private List<Long> getAllTileIds() {
//        List<Long> tileIDs = new ArrayList<>();
//        for (Tile tile : getAllTiles() ) {
//            tileIDs.add(tile.getId());
//        }
//        return tileIDs;
//    }

//    private void deleteTile(Tile tile) {
//        ImageStore.deleteImageFile(Tile.class, tile.getId(), dataDir);
//        Database.getInstance().removeBasicObject(Tile.class, tile);
//    }

    private void readAllTileSets() {
//        Map<Long, Image> images = ImageStore.readAllImages(Tile.class,dataDir);
//        for (Long id : images.keySet()) {
//            Tile tile = findTileById(id);
//            if (tile != null ) {
//                tile.setImage(images.get(id));
//            } else {
//                Logger.info(this, getClass().getName() + ": sanityCheck, have Image for tile " + id +" , but Tile not in Database.");
//                ImageStore.deleteImageFile(Tile.class, id, dataDir);
//            }
//        }

        //TODO For now, read 2 TileSets
        try {
            // Load default Tileset
            Image image = ImageIO.read(new File(dataDir + "/tileset/default_tileset1.png"));
            Long id = Database.getInstance().getNextID();
            TileSet set1 = new TileSet(id, "Default_Set1", image, 32, 32);
            tileSets.add(set1);

            image = ImageIO.read(new File(dataDir + "/tileset/default_tileset2.png"));
            id = Database.getInstance().getNextID();
            TileSet set2 = new TileSet(id, "Default_Set2", image, 48, 48);
            tileSets.add(set2);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //    public Tile findTileById( Long id ) {
//        return Database.getInstance().findById(Tile.class, id);
//    }
//
//    public Tile createTile(Long creatorId, Image image) {
//        Long id = Database.getInstance().getNextID();
//        return createTile(id, creatorId, image);
//    }
//
//    private Tile createTile(Long id, Long creatorId, Image image) {
//        Tile tile = new Tile();
//        tile.setId(id);
//        tile.setImage(image);
//        tile.setCreatorId(creatorId);
//        tile.setOwnerId(creatorId);
//        ImageStore.storeImageObject(tile, dataDir);
//        Database.getInstance().addBasicObject(tile);
//        return tile;
//    }
//
//    public List<Tile> getAllTiles() {
//        return Database.getInstance().getAll(Tile.class);
//    }
    public List<TileSet> getAllTileSets() {
        return tileSets;
    }

    public TileSet getTileSet(Long tileId) {
        TileSet found = null;
        for (TileSet set : tileSets) {
            if ( set.getId().equals(tileId)) {
                found = set;
                break;
            }
        }
        return found;
    }
//
//
//    public List<Tile> geTiles(User user) {
//        List<Tile> list = new ArrayList<>();
//        for (Tile t : getAllTiles()) {
//            if (t.getOwnerId().equals( user.getId())) {
//                list.add(t);
//            }
//        }
//        return list;
//    }
}
