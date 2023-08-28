package nl.hamming.storimapp;        // Moveement

import android.content.Context;

public class STORIMClientApplication  {
    private STORIMClientController storimClientController;
    private static STORIMClientApplication instance;

    private STORIMClientApplication() {
        this.storimClientController = new STORIMClientController();
    }

    public static STORIMClientApplication getInstance() {
        if (instance == null ) {
            instance = new STORIMClientApplication();
        }
        return instance;
    }

    public STORIMClientController getStorimClientController() {
        return storimClientController;
    }
}
