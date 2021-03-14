package com.hamming.storim;

import com.hamming.storim.model.BasicObject;
import com.hamming.storim.model.ImageObject;
import com.hamming.storim.model.Tile;
import com.hamming.storim.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ImageStore {

    public static Map<Long, Image> readAllImages(Class clazz) {
        Map<Long, Image> images = new HashMap<>();
        String dataDir = ServerConfig.getInstance().getDataDirectory();
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

    public static void storeImageObject(ImageObject io) {
        try {
        String dataDir = ServerConfig.getInstance().getDataDirectory();
        String objectDirectoryPath = dataDir.concat(File.separator).concat(io.getClass().getSimpleName().toLowerCase(Locale.ROOT));
        String imageFilename = objectDirectoryPath.concat(File.separator).concat(io.getId().toString());
        File imageFile = new File(imageFilename);
        BufferedImage bufferedImage = ImageUtils.getBufferedImage(io.getImage());
        ImageIO.write(bufferedImage, "jpg", imageFile );
        System.out.println(ImageStore.class.getName() + " - wrote "+ imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteImageFile(Class clazz, Long id) {
        String dataDir = ServerConfig.getInstance().getDataDirectory();
        String objectDirectoryPath = dataDir.concat(File.separator).concat(clazz.getSimpleName().toLowerCase(Locale.ROOT));
        String imageFilename = objectDirectoryPath.concat(File.separator).concat(id.toString());
        File imageFile = new File(imageFilename);
        if (imageFile.isFile()) {
            imageFile.delete();
            System.out.println(ImageStore.class.getName() + " - deleted "+ imageFile.getAbsolutePath());
        }
    }



}
