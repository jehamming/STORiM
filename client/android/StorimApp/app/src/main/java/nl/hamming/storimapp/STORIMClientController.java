package nl.hamming.storimapp;

import android.content.Context;

import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.controllers.ConnectionController;

import nl.hamming.storimapp.controllers.*;

public class STORIMClientController {

    private MicroServerProxy microServerProxy;
    private ConnectionController connectionController;


    public STORIMClientController() {
        init();
    }

    private void init() {
        connectionController = new ConnectionController("STORIM_Android_client");
        microServerProxy = new MicroServerProxy(connectionController);
    }

    public MicroServerProxy getMicroServerProxy() {
        return microServerProxy;
    }
}
