package com.hamming.storim.client.view;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.common.dto.TileSetDto;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TileSet {

    private TileSetDto tileSetDto;
    private BufferedImage bufferedImage;
    private List<Image> tiles;

    public TileSet( TileSetDto tileSetDto ) {
        this.tileSetDto = tileSetDto;
        createTileList();
    }

    public Long getTileSetId() {
        return tileSetDto.getId();
    }

    private void createTileList() {
        Image img = ImageUtils.decode(tileSetDto.getImageData());
        bufferedImage = ImageUtils.getBufferedImage(img);
        tiles = new ArrayList<>();
        int tileSetWidth = bufferedImage.getWidth(null);
        int tileSetHeight = bufferedImage.getHeight(null);
        int tileHeight = tileSetDto.getTileHeight();
        int tileWidth = tileSetDto.getTileWidth();
        int nbrTileSetRows = tileSetWidth / tileWidth;
        int nbrTileSetCols = tileSetHeight / tileHeight;
        for (int c = 0; c < nbrTileSetCols; c++) {
            for (int r = 0; r < nbrTileSetRows; r++) {
                int y = c * tileWidth;
                int x = r * tileHeight;
                BufferedImage tileImage = bufferedImage.getSubimage(x, y, tileWidth, tileHeight);
                tiles.add(tileImage);
            }
        }
    }

    public List<Image> getTiles() {
        return tiles;
    }

    public Image getTile(int index) {
        Image retval = null;
        if ( index >= 0 && index < tiles.size() ) {
            retval = tiles.get(index);
        }
        return retval;
    }

    public int getTileHeight() {
        return tileSetDto.getTileHeight();
    }

    public int getTileWidth() {
        return tileSetDto.getTileWidth();
    }
}
