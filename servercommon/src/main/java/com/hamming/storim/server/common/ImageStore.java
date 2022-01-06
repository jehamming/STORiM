package com.hamming.storim.server.common;


import com.hamming.storim.server.common.model.ImageObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ImageStore {

    public static Map<Long, Image> readAllImages(Class clazz, String dataDir) {
        Map<Long, Image> images = new HashMap<>();
        String objectDirectoryPath = dataDir.concat(File.separator).concat(clazz.getSimpleName().toLowerCase(Locale.ROOT));
        File imageDir = new File(objectDirectoryPath);
        if ( imageDir.isDirectory() ) {
            for (File imageFile : imageDir.listFiles()) {
                try {
                Image image = ImageIO.read(imageFile);
                Long id = Long.valueOf(imageFile.getName());
                images.put(id, image);
                System.out.println(ImageStore.class.getName() + " - read "+ imageFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return images;
    }

    public static void storeImageObject(ImageObject io, String dataDir) {
        try {
        String objectDirectoryPath = dataDir.concat(File.separator).concat(io.getClass().getSimpleName().toLowerCase(Locale.ROOT));
        String imageFilename = objectDirectoryPath.concat(File.separator).concat(io.getId().toString());
        File imageFile = new File(imageFilename);
        BufferedImage bufferedImage = ImageUtils.getBufferedImage(io.getImage());
        ImageIO.write(bufferedImage, "png", imageFile );
        System.out.println(ImageStore.class.getName() + " - wrote "+ imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteImageFile(Class clazz, Long id, String dataDir) {
        String objectDirectoryPath = dataDir.concat(File.separator).concat(clazz.getSimpleName().toLowerCase(Locale.ROOT));
        String imageFilename = objectDirectoryPath.concat(File.separator).concat(id.toString());
        File imageFile = new File(imageFilename);
        if (imageFile.isFile()) {
            imageFile.delete();
            System.out.println(ImageStore.class.getName() + " - deleted "+ imageFile.getAbsolutePath());
        }
    }



}
