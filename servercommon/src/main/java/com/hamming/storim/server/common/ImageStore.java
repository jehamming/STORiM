package com.hamming.storim.server.common;


import com.hamming.storim.common.util.Logger;

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
                Logger.info (ImageStore.class.getSimpleName() + " - read "+ imageFile.getAbsolutePath());
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
        if ( imageFile.exists()) {
            imageFile.delete();
        }
        BufferedImage bufferedImage = ImageUtils.getBufferedImage(io.getImage());
        ImageIO.write(bufferedImage, "png", imageFile );
        Logger.info(ImageStore.class.getSimpleName() + " - wrote "+ imageFile.getAbsolutePath());
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
            Logger.info(ImageStore.class.getSimpleName() + " - deleted "+ imageFile.getAbsolutePath());
        }
    }



}
