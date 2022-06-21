package com.hamming.storim.server;

import com.hamming.storim.common.util.Logger;

public class ShowDatabase {

    public void showDatabase(String databaseFile) {
        Database.getInstance(databaseFile);
        for (Class c : Database.getInstance().getClassTypes()) {
            for (Object item : Database.getInstance().getAll( c ) ) {
                Logger.info(this, item.toString());
            }
        }
    }

    public static void main(String[] args) {
        String databaseFile = args[0];
        ShowDatabase sd = new ShowDatabase();
        sd.showDatabase(databaseFile);
    }
}
