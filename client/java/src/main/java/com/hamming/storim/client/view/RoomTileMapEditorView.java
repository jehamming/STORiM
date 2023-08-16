package com.hamming.storim.client.view;


import com.hamming.storim.client.controller.RoomTileMapEditorPanelController;
import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.common.view.Action;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Timer;
import java.util.*;

public class RoomTileMapEditorView extends JPanel implements Runnable {

    //boolean variable to track if the game is playing or not
    private int fps = 30;
    private long time;
    private BufferedImage backBuffer;
    private boolean handlingActions;
    boolean drawGrid = true;
    boolean drawFG = true;
    boolean drawBG = true;
    private int[][] backgroundTileMap;
    private int[][] foregroundTileMap;

    private TileSet backgroundTileSet;
    private TileSet foregroundTileSet;

    //the game thread
    private Thread actionHandlerThread = null;

    private List<Action> actions;

    public RoomDto room;

    //
    // TODO RoomEditorController
    private RoomTileMapEditorPanelController roomTileMapEditorPanelController;

    Timer timer;
    TimerTask task;

    private float unitX = 1f;
    private float unitY = 1f;


    //Class constructor
    public RoomTileMapEditorView(RoomTileMapEditorPanelController controller) {
        this.roomTileMapEditorPanelController = controller;
        this.actions = Collections.synchronizedList(new LinkedList<>());
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(p, RoomTileMapEditorView.this);
                if ( room != null ) {
                    int row = getRow(p.y);
                    int col = getCol(p.x);
                    boolean deleteTile = e.isControlDown();
                    controller.applyTile(deleteTile, row, col);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    @Override
    public void run() {
        while (handlingActions) {
            time = System.currentTimeMillis();
            handleActions();
            waitIfNeeded();
        }
    }

    public void scheduleAction(Action action) {
        synchronized (actions) {
            actions.add(action);
        }
    }


    private void handleActions() {
        synchronized (actions) {
            for (Action action : actions) {
                action.execute();
                repaint();
            }
            actions.removeAll(actions);
        }
    }

    public RoomDto getRoom() {
        return room;
    }

    public void setRoom(RoomDto room) {
        this.room = room;
        if ( room != null ) {
            backgroundTileMap = room.getBackTileMap();
            foregroundTileMap = room.getFrontTileMap();
        } else {
            foregroundTileMap = null;
            backgroundTileMap = null;
        }
        repaint();
    }


    private void waitIfNeeded() {
        time = (1000 / fps) - (System.currentTimeMillis() - time);
        if (time > 0) {
            try {
                Thread.sleep(time);
            } catch (Exception e) {
            }
        }
        try {
            actionHandlerThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void start() {
        handlingActions = true;
        actionHandlerThread = new Thread(this);
        actionHandlerThread.start();
    }

    @Override
    public void paint(Graphics g) {
        Logger.info("Painting");
        backBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics bbg = backBuffer.getGraphics();

        bbg.setColor(Color.black);
        bbg.fillRect(0, 0, getWidth(), getHeight());
        if ( room != null ) {
            if (drawBG & backgroundTileSet != null & backgroundTileMap != null ) {
                drawTiles(bbg, backgroundTileSet, backgroundTileMap);
            }
            if (drawFG & foregroundTileSet != null & foregroundTileMap != null ) {
                drawTiles(bbg, foregroundTileSet, foregroundTileMap);
            }

            if ( drawGrid ) drawGrid(bbg);
        }
        g.drawImage(backBuffer, 0, 0, this);
    }

    private void drawGrid(Graphics g) {
        Color oldColor = g.getColor();
        g.setColor(Color.DARK_GRAY);
        if (room != null) {
            int rows = room.getRows();
            int cols = room.getCols();

            int widthPerTile = getWidth() / cols;
            int heightPerTile = getHeight() / rows;

            for (int c = 0; c < cols; c++) {
                int x = c * widthPerTile;
                g.drawLine(x, 0, x, getHeight());
            }
            for (int r = 0; r < rows; r++) {
                int y = r * heightPerTile;
                g.drawLine(0, y, getWidth(), y);
            }
            g.setColor(oldColor);
        }
    }


    private void drawTiles(Graphics g, TileSet tileSet, int[][] tileMap) {
        //Draw Tiles
        int rows = room.getRows();
        int cols = room.getCols();

        int widthPerTile = getWidth() / cols;
        int heightPerTile = getHeight() / rows;
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                int x = c * widthPerTile;
                int y = r * heightPerTile;
                int width = widthPerTile;
                int height = heightPerTile;
                //Check last tiles (row/col)
                if (c == cols) {
                    width = getWidth() - x;
                }
                if ( r == rows ) {
                    height = getHeight() - y;
                }
                if ( tileMap[c][r] >= 0 ) {
                    Image tileImage = tileSet.getTile(tileMap[c][r]);
                    g.drawImage(tileImage, x, y, width, height, this);
                }
            }
        }
    }

    private int getRow(int y) {
        //Draw Tiles
        int rows = room.getRows();
        int heightPerTile = getHeight() / rows;
        int row = y / heightPerTile;
        row++;
        if (row  > rows ) row = rows;
        return row;
    }

    private int getCol(int x) {
        //Draw Tiles
        int cols = room.getCols();
        int widthPerTile = getWidth() / cols;
        int col = x / widthPerTile;
        col++;
        if ( col > cols ) col = cols;
        return col;
    }



    public void resetView() {
        setRoom(null);
    }

    public void setBackgroundTileSet(TileSet tileSet) {
        this.backgroundTileSet = tileSet;
        if (room != null ) resetBackgroundTileMap();
        repaint();
    }

    public void setForegroundTileSet(TileSet tileSet) {
        this.foregroundTileSet = tileSet;
        if (room != null ) resetForegroundTileMap();
        repaint();
    }
    private void resetBackgroundTileMap() {
        backgroundTileMap = new int[room.getCols()][room.getRows()];
        fillTileMap(backgroundTileMap, -1);
        repaint();
    }

    private void resetForegroundTileMap() {
        foregroundTileMap = new int[room.getCols()][room.getRows()];
        fillTileMap(foregroundTileMap, -1);
        repaint();
    }


    private void fillTileMap(int[][] newTileMap, int value) {
        for (int c = 0; c < room.getCols(); c++) {
            for (int r = 0; r < room.getRows(); r++) {
                newTileMap[c][r] = value;
            }
        }
    }


    public void applyBackgroundTile(int tileIndex, int row, int col) {
        backgroundTileMap[col-1][row-1] = tileIndex;
    }

    public void applyForegroundTile(int tileIndex, int row, int col) {
        foregroundTileMap[col-1][row-1] = tileIndex;
    }

    public void applyTileToAllBackground(int tileIndex) {
        for (int c = 0; c < room.getCols(); c++) {
            for (int r = 0; r < room.getRows(); r++) {
                backgroundTileMap[c][r] = tileIndex;
            }
        }
    }

    public void setGrid(boolean selected) {
        drawGrid= selected;
        repaint();
    }

    public int[][] getBackgroundTileMap() {
        return backgroundTileMap;
    }

    public int[][] getForegroundTileMap() {
        return foregroundTileMap;
    }

    public void setDrawForeGround(boolean isSelected) {
        drawFG = isSelected;
    }

    public void setDrawBackground(boolean isSelected) {
        drawBG = isSelected;
    }
}


