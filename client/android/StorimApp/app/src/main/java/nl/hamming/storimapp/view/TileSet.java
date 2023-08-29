package nl.hamming.storimapp.view;

import android.graphics.Bitmap;

import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.util.Logger;

import java.util.ArrayList;
import java.util.List;

import nl.hamming.storimapp.ImageUtils;

public class TileSet {

    private TileSetDto tileSetDto;
    private List<Bitmap> tiles;

    public TileSet(TileSetDto tileSetDto) {
        this.tileSetDto = tileSetDto;
        createTileList();
    }

    private void createTileList() {
        Bitmap tileSetImage = ImageUtils.decode(tileSetDto.getImageData());
        tiles = new ArrayList<>();
        int tileSetWidth = tileSetImage.getWidth();
        int tileSetHeight = tileSetImage.getHeight();
        int tileHeight = tileSetDto.getTileHeight();
        int tileWidth = tileSetDto.getTileWidth();
        int nbrTileSetRows = tileSetWidth / tileWidth;
        int nbrTileSetCols = tileSetHeight / tileHeight;
        for (int c = 0; c < nbrTileSetCols; c++) {
            for (int r = 0; r < nbrTileSetRows; r++) {
                int y = c * tileWidth;
                int x = r * tileHeight;
                try {
                    Bitmap tileImage = Bitmap.createBitmap(tileSetImage, x, y, tileWidth, tileHeight);
                    tiles.add(tileImage);
                } catch (Exception e) {
                    Logger.error("Exception: " + e.getMessage());
                }
            }
        }

    }

    public List<Bitmap> getTiles() {
        return tiles;
    }

    public Bitmap getTile(int index) {
        Bitmap retval = null;
        if (index >= 0 && index < tiles.size()) {
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

    public String getName() {
        return tileSetDto.getName();
    }

    public Long getId() {
        return tileSetDto.getId();
    }
}
