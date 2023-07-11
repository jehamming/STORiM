package com.hamming.storim.common.net;

import com.hamming.storim.common.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerConfig {

    private final static String DATADIR     = "datadir";
    private final static String SERVERPORT  = "serverport";

    public final static String PROTOCOL  = "storim";


    // Properties file location
    private Properties properties;

    private static ServerConfig instance;

    private ServerConfig(String propertiesFile) {
        loadProperties(propertiesFile);
    };

    public static ServerConfig getInstance(String propertiesFile) {
        if ( instance == null ) {
            instance = new ServerConfig(propertiesFile);
        }
        return instance;
    }

    public void loadProperties(String propertiesFile) {
        // Load properties file
        properties = new Properties();
        try {
            File configFile = new File(propertiesFile);
            FileInputStream fis = new FileInputStream(configFile);
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPropertyAsString( String name ) {
        String value = properties.getProperty(name);
        if (value == null) {
            Logger.info(this, ":" + "Error loading property '" + name +"', no value found!");
        }
        return value;
    }

    public int getPropertyAsInt( String name ) {
        Integer value = Integer.valueOf(properties.getProperty(name));
        if (value == null) {
            Logger.info(this, ":" + "Error loading property '" + name +"', no value found!");
        }
        return value;
    }


}
