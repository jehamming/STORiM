package nl.hamming.storimapp.view;

import android.graphics.Bitmap;

import nl.hamming.storimapp.ImageUtils;

public class Exit extends BasicDrawableObject {

    private float scale = 1;
    private int rotation = 0;
    private Bitmap scaledImage;

    private String name;

    private String toRoomURI;


    public Exit(Long exitId, String name) {
        setId(exitId);
        this.scaledImage = null;
        this.toRoomURI = null;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToRoomURI() {
        return toRoomURI;
    }

    public void setToRoomURI(String toRoomURI) {
        this.toRoomURI = toRoomURI;
    }
}
