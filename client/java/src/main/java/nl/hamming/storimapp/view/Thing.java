package nl.hamming.storimapp.view;

import com.hamming.storim.util.ImageUtils;

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
        BufferedImage image = (BufferedImage) super.getImage();
        scaledImage = ImageUtils.scaleImage(image, scale);
        scaledImage = ImageUtils.rotateImage(scaledImage, rotation);
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
