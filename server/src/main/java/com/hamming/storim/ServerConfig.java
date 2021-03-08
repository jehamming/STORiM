package com.hamming.storim;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerConfig {

    private final static String DATADIR     = "datadir";
    private final static String SERVERPORT  = "serverport";
    private final static String USERSFILE   = "usersfile";
    private final static String ROOMSFILE  = "roomsfile";
    private final static String THINGSFILE  = "thingsfile";

    // Properties file location
    private final static String propertiesFile = "server.properties";
    private Properties properties;

    // Defaults
    private String dataDirectory = "data";
    private int serverPort = 3131;
    private String usersDataFile = "Users.dat";
    private String roomsDataFile = "Rooms.dat";
    private String thingsDataFile = "Things.dat";


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
            setUsersDataFile(loadProperty(USERSFILE));
            setRoomsDataFile(loadProperty(ROOMSFILE));
            setThingsDataFile(loadProperty(THINGSFILE));
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


    public String getUsersDataFile() {
        return usersDataFile;
    }

    public void setUsersDataFile(String usersDataFile) {
        this.usersDataFile = usersDataFile;
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

    public String getRoomsDataFile() {
        return roomsDataFile;
    }

    public void setRoomsDataFile(String roomsDataFile) {
        this.roomsDataFile = roomsDataFile;
    }

    public String getThingsDataFile() {
        return thingsDataFile;
    }

    public void setThingsDataFile(String thingsDataFile) {
        this.thingsDataFile = thingsDataFile;
    }
}
