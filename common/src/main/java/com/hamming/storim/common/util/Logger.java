package com.hamming.storim.common.util;

public class Logger {

    public static void info(Object o, String txt) {
        info( o.getClass().getSimpleName() + " - " + txt);
    }

    public static void info(Object o, String id, String txt) {
        String idText = "";
        if (id != null) {
            idText = id + "-" ;
        }
        info( idText + o.getClass().getSimpleName()+ " - "+ txt);
    }

    public static void info(String txt) {
        System.out.println(txt);
    }
}
