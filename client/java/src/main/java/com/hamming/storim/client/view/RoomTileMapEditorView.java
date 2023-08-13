package com.hamming.storim.client.view;


import com.hamming.storim.client.controller.RoomTileMapEditorPanelController;
import com.hamming.storim.common.dto.*;
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
    private boolean drawing;
    private int[][] tileMap;
    boolean drawGrid = true;

    //the game thread
    private Thread drawThread = null;

    private List<Action> actions;

    public RoomDto room;
    public TileSet tileSet;

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
                int row = getRow(p.y);
                int col = getCol(p.x);
                controller.applyTile(row, col);
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
        while (drawing) {
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

    private void determineUnitXY() {
//        unitX = (float) getWidth() / (float) room.getWidth();
//        unitY = (float) getHeight() / (float) room.getLength();
    }

    public void componentResized() {
        if (room != null) {
            determineUnitXY();
        }
    }

    public void setRoom(RoomDto room) {
        this.room = room;
        if ( room != null ) {
            setTileMap(room.getTileMap());
            determineUnitXY();
        } else {
            resetTileMap();
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
            drawThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void start() {
        drawing = true;
        drawThread = new Thread(this);
        drawThread.start();
    }

    @Override
    public void paint(Graphics g) {
        if ( room != null ) {
            backBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics bbg = backBuffer.getGraphics();

            bbg.setColor(Color.black);
            bbg.fillRect(0, 0, getWidth(), getHeight());
            drawTiles(bbg);

            if ( drawGrid ) {
                drawGrid(bbg);
            }

            g.drawImage(backBuffer, 0, 0, this);
        } else {
            backBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics bbg = backBuffer.getGraphics();

            bbg.setColor(Color.black);
            bbg.fillRect(0, 0, getWidth(), getHeight());
        }

    }

    private void drawGrid(Graphics g) {
        Color oldColor = g.getColor();
        g.setColor(Color.DARK_GRAY);
        if (room != null && tileSet != null) {
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


    private void drawTiles(Graphics g) {
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

    public void setTileSet(TileSet tileSet) {
        this.tileSet = tileSet;
        repaint();
    }

    private void resetTileMap() {
        if ( room != null ) {
            tileMap = new int[room.getCols()][room.getRows()];
            for (int c = 0; c < room.getCols(); c++) {
                for (int r = 0; r < room.getRows(); r++) {
                    tileMap[c][r] = -1;
                }
            }
        }
    }

    public void applyTile(int tileIndex, int row, int col) {
        tileMap[col-1][row-1] = tileIndex;
    }
    public void applyToAll(int tileIndex) {
        for (int c = 0; c < room.getCols(); c++) {
            for (int r = 0; r < room.getRows(); r++) {
                tileMap[c][r] = tileIndex;
            }
        }
    }

    public void setGrid(boolean selected) {
        drawGrid= selected;
        repaint();
    }


    public void setTileMap(int[][] newTileMap) {
        tileMap = newTileMap;
    }

    public int[][] getTileMap() {
        return tileMap;
    }
}


