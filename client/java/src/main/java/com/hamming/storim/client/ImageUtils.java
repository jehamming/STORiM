package com.hamming.storim.client;

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
        return image;
    }


    public static byte[] encode(Image image) {
        byte[] imageData = new byte[0];
        try {
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bufferedImage.createGraphics();
            g2.drawImage(image, null, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
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

    public static Image resize(Image image, int scaledWidth, int scaledHeight) {

        BufferedImage inputImage = (BufferedImage) image;

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        return outputImage;
    }

    public static BufferedImage rotateImage(BufferedImage original, int degrees) {
        double theta = Math.toRadians(degrees);
        double cos = Math.abs(Math.cos(theta));
        double sin = Math.abs(Math.sin(theta));
        double width = original.getWidth();
        double height = original.getHeight();
        int w = (int) (width * cos + height * sin);
        int h = (int) (width * sin + height * cos);

        BufferedImage out = new BufferedImage(w, h, original.getType());
        Graphics2D g2 = out.createGraphics();
        double x = w / 2; //the middle of the two new values
        double y = h / 2;

        AffineTransform at = AffineTransform.getRotateInstance(theta, x, y);
        x = (w - width) / 2;
        y = (h - height) / 2;
        at.translate(x, y);
        g2.drawRenderedImage(original, at);
        g2.dispose();

        return out;
    }

    public static BufferedImage scaleImage(BufferedImage image, float scale) {
        return scaleImage(image, scale, scale);
    }

    public static BufferedImage scaleImage(BufferedImage image, float scaleWidth, float scaleHeight) {
        int width = Float.valueOf(image.getWidth(null) * scaleWidth).intValue();
        int height = Float.valueOf(image.getHeight(null) * scaleHeight).intValue();
        BufferedImage out = new BufferedImage(width, height, image.getType());
        Graphics2D g2 = out.createGraphics();
        g2.drawImage(image, 0, 0, width, height, null);
        g2.dispose();
        return out;
    }
}
