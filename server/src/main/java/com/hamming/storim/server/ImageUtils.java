package com.hamming.storim.server;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {
    public static Image decode(byte[] data) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  image;
    }


    public static byte[] encode(Image image) {
        byte[] imageData = new byte[0];
        try {
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bufferedImage.createGraphics();
            g2.drawImage(image, null, null);
            ByteArrayOutputStream baos= new ByteArrayOutputStream(1000);
            ImageIO.write(bufferedImage, "png", baos);
            imageData = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageData;
    }

    public static BufferedImage getBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, null, null);
        return bufferedImage;
    }

}
