package com.hamming.storim.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.regex.Pattern;

public class StringUtils {

    private static String delimiter="#_#";

    public static String getDelimiter() {
        return delimiter;
    }

    public static String combineValuesToString(String ...items) {
        return String.join(getDelimiter(), items);
    }

    public static String combineValuesToReadableString(String ...items) {
        return String.join(" ", items).trim();
    }

    public static String hashPassword(String password) {
        // Create MessageDigest instance for MD5
        MessageDigest md = null;
        String hashedPassword = null;
        try {
            md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(password.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            hashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    public static String replace(String input, Map<String,String> replacements) {
        for (String key: replacements.keySet() ) {
            String newValue = replacements.get(key);
            String replaceKey = "${"+key+"}";
            input = input.replaceAll(Pattern.quote(replaceKey), newValue);
        }
        return input;
    }

}
