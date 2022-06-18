package com.hamming.storim.server;


import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.common.model.BasicObject;

import java.io.*;
import java.util.*;

public class Database {

    private long lastId = 0;
    private String databaseFilename;
    private static Database instance;

    private Map<Class, List<BasicObject>> data;

    private Database(String databaseFilename) {
        this.databaseFilename = databaseFilename;
        data = new HashMap<>();
        load();
    }

    public static Database getInstance(String databaseFilename) {
        if (instance == null ) {
            instance = new Database(databaseFilename);
        }
        return instance;
    }

    public static Database getInstance() {
        if (instance == null ) {
            System.err.println("Database needs to be initialized first!");
        }
        return instance;
    }

    public void addBasicObject(BasicObject o) {
        addBasicObject(o.getClass(), o);
    }


    private void addBasicObject(Class c, BasicObject o) {
        List<BasicObject> listOfObject = data.get(c);
        if (listOfObject == null) {
            listOfObject = new ArrayList<>();
            data.put(c,listOfObject);
        }
        listOfObject.add(o);
    }

    public void removeBasicObject(BasicObject o) {
        removeBasicObject(o.getClass(), o);
    }

    public void removeBasicObject(Class c, BasicObject o) {
        List<BasicObject> listOfObjects = data.get(c);
        if (listOfObjects != null && listOfObjects.contains(o)) {
            listOfObjects.remove(o);
        }
    }

    public <T extends BasicObject> T findById(Class<T> c, Long id) {
        T found = null;
        List<BasicObject> listOfObjects = data.get(c);
        if (listOfObjects != null) {
            for (BasicObject anObject : listOfObjects) {
                if (anObject.getId().equals(id)) {
                    found = (T) anObject;
                    break;
                }
            }
        }
        return found;
    }

    public <T extends BasicObject> List<T> getAll(Class<T> c) {
        List<T> listOfObjects = ( List<T> ) data.get(c);
        if (listOfObjects == null ) {
            listOfObjects = new ArrayList<T>();
        }
        return listOfObjects;
    }

    public <T extends BasicObject> List<T> getAll(Class<T> c, Long ownerId) {
        List<T> listOfOwnedObjects = new ArrayList<T>();
        List<T> allClassObjects = ( List<T> ) data.get(c);
        if (allClassObjects != null ) {
            for (T basicObject : allClassObjects) {
                if (basicObject.getOwnerId().equals(ownerId)) {
                    listOfOwnedObjects.add(basicObject);
                }
            }
        }
        return listOfOwnedObjects;
    }

    public Long getNextID() {
        lastId = lastId + 1;
        return lastId;
    }

    public void setLastAddedID(Long id) {
        if ( id > lastId) {
            lastId = id;
        }
    }

    private Long getHighestID() {
        Long highest = 0L;
        for (List<BasicObject> list : data.values()) {
            for (BasicObject b: list) {
                long id = b.getId();
                if (id > highest) {
                    highest = id;
                }
            }
        }
        return highest;
    }


    public void store() {
        File file = new File(databaseFilename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
            Logger.info(this, getClass().getName() + ": Stored Database in file "+ file.getAbsolutePath());
            printDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File(databaseFilename);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            data = (Map<Class, List<BasicObject>>) ois.readObject();
            setLastAddedID(getHighestID());
            ois.close();
            Logger.info(this, "Loaded Database from file "+ file.getAbsolutePath());
            printDatabase();
        } catch (IOException | ClassNotFoundException e) {
            Logger.info(this, ":" + "ERROR:" + e.getMessage());
            //e.printStackTrace();
        }
    }

    private void printDatabase() {
       Logger.info(this, "Database content:");
        Set<Class> classes = data.keySet();
        for (Class c : classes) {
            long count = data.get(c).size();
            System.out.print(c.getSimpleName() + "(" + count +") ");
        }
        System.out.println();
        Logger.info(this, "Highest ID = " + lastId);
    }

    public void clearDatabase() {
        data = new HashMap<Class, List<BasicObject>>();
        lastId = 0;
    }

    public Set<Class> getClassTypes() {
        return data.keySet();
    }


}
