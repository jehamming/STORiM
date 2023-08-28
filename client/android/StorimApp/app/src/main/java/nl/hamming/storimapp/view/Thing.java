package nl.hamming.storimapp.view;

import android.graphics.Bitmap;

import nl.hamming.storimapp.ImageUtils;

public class Thing extends BasicDrawableObject {

    private float scale = 1;
    private int rotation = 0;
    private Bitmap scaledImage;


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
        Bitmap image =  super.getImage();
        scaledImage = ImageUtils.scaleImage(image, scale);
    }

    @Override
    public Bitmap getImage() {
        return scaledImage;
    }

    @Override
    public void setImage(Bitmap image) {
        super.setImage(image);
        updateScaledImage();
    }
}
