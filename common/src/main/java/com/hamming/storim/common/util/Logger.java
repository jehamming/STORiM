package com.hamming.storim.common.util;

public class Logger {

    public static void info(Object o, String txt) {
        System.out.println(o.getClass().getSimpleName() + " - " + txt);
    }
}
