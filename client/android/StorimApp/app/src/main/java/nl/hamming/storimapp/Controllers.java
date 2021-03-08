package nl.hamming.storimapp;

import nl.hamming.storimapp.controllers.*;

public class Controllers {


    private ConnectionController connectionController;
    private MoveController moveController;
    private UserController userController;
    private ViewController viewController;



    public ConnectionController getConnectionController() {
        return connectionController;
    }

    public void setConnectionController(ConnectionController connectionController) {
        this.connectionController = connectionController;
    }


    public MoveController getMoveController() {
        return moveController;
    }

    public void setMoveController(MoveController moveController) {
        this.moveController = moveController;
    }

    public UserController getUserController() {
        return userController;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public ViewController getViewController() {
        return viewController;
    }

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }
}
