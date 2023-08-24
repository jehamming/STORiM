package com.hamming.storim.server.common;

import java.io.File;
import java.nio.file.Files;

public class FileUtils {
    public static void checkDirectory(String dir) {
        File dirFile = new File(dir);
        if ( !dirFile.exists() ) {
            dirFile.mkdir();
        }
    }
}
