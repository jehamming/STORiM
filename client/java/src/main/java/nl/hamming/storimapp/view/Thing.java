package nl.hamming.storimapp.view;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Thing extends BasicDrawableObject {

    private float scale = 1;
    private int rotation = 0;
    private BufferedImage scaledImage;


    public Thing(Long thingId) {
        setId(thingId);
        scaledImage = null;
    }

    public void setScale(float scale) {
        this.scale = scale;
        updateScaledImage();
    }


    public void setRotation(int rotation) {
        this.rotation = rotation;
        updateScaledImage();
    }

    private void updateScaledImage() {
        Image image = super.getImage();
        int width  = new Float( image.getWidth(null) * scale ).intValue();
        int height = new Float( image.getHeight(null) * scale ).intValue();

        scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();

        double rotationAngle = Math.toRadians (rotation);
        int rx = (width/2);
        int ry = (height/2);
        //Make a backup so that we can reset our graphics object after using it.
        AffineTransform backup = g2d.getTransform();
        //rx is the x coordinate for rotation, ry is the y coordinate for rotation, and angle
        //is the angle to rotate the image. If you want to rotate around the center of an image,
        //use the image's center x and y coordinates for rx and ry.
        AffineTransform a = AffineTransform.getRotateInstance(rotationAngle, rx, ry);
        //Set our Graphics2D object to the transform
        g2d.setTransform(a);
        //Draw our image like normal
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.setTransform(backup);
    }

    @Override
    public Image getImage() {
        return scaledImage;
    }

    @Override
    public void setImage(Image image) {
        super.setImage(image);
        updateScaledImage();
    }
}
