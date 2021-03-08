package nl.hamming.storimapp;        // Moveement

import android.content.Context;
import android.content.Intent;

import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.model.dto.UserDto;

import nl.hamming.storimapp.controllers.ConnectionController;
import nl.hamming.storimapp.controllers.MoveController;
import nl.hamming.storimapp.controllers.UserController;
import nl.hamming.storimapp.controllers.ViewController;
import nl.hamming.storimapp.interfaces.ConnectionListener;
import nl.hamming.storimapp.interfaces.UserListener;
import nl.hamming.storimapp.ui.LoginActivity;
import nl.hamming.storimapp.ui.MainActivity;
import nl.hamming.storimapp.view.GameView;

public class STORIMClientApplication  {
    private Controllers controllers;
    private ProtocolHandler protocolHandler;
    private static STORIMClientApplication instance;
    private Context context;

    private STORIMClientApplication(Context context) {
        this.protocolHandler = new ProtocolHandler();
        this.context = context;
        initControllers();
    }

    public static STORIMClientApplication getInstance(Context context) {
        if (instance == null ) {
            instance = new STORIMClientApplication(context);
        }
        return instance;
    }

    public void initControllers() {
        controllers = new Controllers();
        // Connection
        controllers.setConnectionController(new ConnectionController());
        // Users
        controllers.setUserController( new UserController(controllers.getConnectionController()));
        // Moveement
        controllers.setMoveController(new MoveController(controllers));
    }

    private void setGameView(GameView view) {
        controllers.setViewController(new ViewController(view, controllers));
    }

    public Controllers getControllers() {
        return controllers;
    }


}
