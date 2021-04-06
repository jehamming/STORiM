package com.hamming.storim.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerConfig {

    private final static String DATADIR     = "datadir";
    private final static String SERVERPORT  = "serverport";


    // Properties file location
    private final static String propertiesFile = "server.properties";
    private Properties properties;

    // Defaults
    private String dataDirectory = "data";
    private int serverPort = 3131;



    private static ServerConfig instance;

    private ServerConfig() {
        loadProperties();
    };

    public static ServerConfig getInstance() {
        if ( instance == null ) {
            instance = new ServerConfig();
        }
        return instance;
    }

    public void loadProperties() {
        // Load properties file
        properties = new Properties();
        try {
            File configFile = new File(propertiesFile);
            FileInputStream fis = new FileInputStream(configFile);
            properties.load(fis);
            setDataDirectory(loadProperty(DATADIR));
            setServerPort(Integer.valueOf(loadProperty(SERVERPORT)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String loadProperty( String name ) {
        String value = properties.getProperty(name);
        if (value == null) {
            System.out.println(this.getClass().getName() + ":" + "Error loading property '" + name +"', no value found!");
        }
        return value;
    }



    public String getDataDirectory() {
        return dataDirectory;
    }

    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }


}
