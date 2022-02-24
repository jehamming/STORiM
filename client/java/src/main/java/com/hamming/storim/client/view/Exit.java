package com.hamming.storim.client.view;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Exit extends BasicDrawableObject {

    public static enum Orientation { NORTH, SOUTH, EAST, WEST };

    private Orientation orientation;
    private String name;

    public Exit(Long exitId, String name, Orientation orientation) {
        setId(exitId);
        this.name = name;
        this.orientation = orientation;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public String getName() {
        return name;
    }

    @Override
    public Image getImage() {
        if ( super.getImage() == null ) {
            int WIDTH_NS = 60;
            int HEIGHT_EW = 60;
            int WIDTH_EW = 15;
            int HEIGHT_NS = 15;
            Color c = Color.LIGHT_GRAY;
            BufferedImage image = null;
            java.awt.Graphics g = null;
            switch (orientation) {
                case NORTH:
                    image = new BufferedImage(WIDTH_NS, HEIGHT_NS, BufferedImage.TYPE_INT_RGB);
                    g = image.getGraphics();
                    g.setColor(c);
                    g.fillRect(0, 0, WIDTH_NS, HEIGHT_NS);
                    break;
                case SOUTH:
                    image = new BufferedImage(WIDTH_NS, HEIGHT_NS, BufferedImage.TYPE_INT_RGB);
                    g = image.getGraphics();
                    g.setColor(c);
                    g.fillRect(0, 0, WIDTH_NS, HEIGHT_NS);
                    break;
                case EAST:
                    image = new BufferedImage(WIDTH_EW, HEIGHT_EW, BufferedImage.TYPE_INT_RGB);
                    g = image.getGraphics();
                    g.setColor(c);
                    g.fillRect(0, 0, WIDTH_EW, HEIGHT_EW);
                    break;
                case WEST:
                    image = new BufferedImage(WIDTH_EW, HEIGHT_EW, BufferedImage.TYPE_INT_RGB);
                    g = image.getGraphics();
                    g.setColor(c);
                    g.fillRect(0, 0, WIDTH_EW, HEIGHT_EW);
                    break;
            }
            setImage(image);
        }
        return super.getImage();
    }
}
