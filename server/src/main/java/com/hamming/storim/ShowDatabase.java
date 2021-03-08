package com.hamming.storim;

public class ShowDatabase {

    public void showDatabase() {
        for (Class c : Database.getInstance().getClassTypes()) {
            for (Object item : Database.getInstance().getAll( c ) ) {
                System.out.println(item.toString());
            }
        }
    }

    public static void main(String[] args) {
        ShowDatabase sd = new ShowDatabase();
        sd.showDatabase();
    }
}
