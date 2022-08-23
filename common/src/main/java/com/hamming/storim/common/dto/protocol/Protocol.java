package com.hamming.storim.common.dto.protocol;

import com.hamming.storim.common.util.Logger;

import java.util.HashMap;
import java.util.List;

public class Protocol {

    private static Protocol instance;
    private HashMap<String, Class> registeredClasses;

    private Protocol() {
        registeredClasses = new HashMap<>();
    }

    public static Protocol getInstance() {
        if ( instance == null ) {
            instance = new Protocol();
        }
        return instance;
    }

    public void registerClass(String name, Class clazz) {
        if ( registeredClasses.get(name) == null ) {
            registeredClasses.put(name,clazz);
        }
    }

    public Class getClass(String name) {
        return  registeredClasses.get(name);
    }

}
