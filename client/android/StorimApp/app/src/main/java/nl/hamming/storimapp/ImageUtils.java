package nl.hamming.storimapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtils {


    public static Bitmap decode(byte[] data) {
        Bitmap image = BitmapFactory.decodeByteArray(data,0,data.length);
        return image;
    }

    public static Bitmap resize(Bitmap image, int scaledWidth, int scaledHeight) {
        return Bitmap.createScaledBitmap(image, scaledWidth, scaledHeight, false);
    }


    public static Bitmap scaleImage(Bitmap image, float scale) {
        return scaleImage(image, scale, scale);
    }

    public static Bitmap scaleImage(Bitmap image, float scaleWidth, float scaleHeight) {
        int width = Float.valueOf(image.getWidth() * scaleWidth).intValue();
        int height = Float.valueOf(image.getHeight() * scaleHeight).intValue();
        return Bitmap.createScaledBitmap(image, width, height, false);
    }
}
