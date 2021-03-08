package com.hamming.storim;

import com.hamming.storim.controllers.*;
import com.hamming.storim.interfaces.ViewerController;

public class Controllers {


    private ConnectionController connectionController;
    private MoveController moveController;
    private UserController userController;
    private VerbController verbController;
    private RoomController roomController;

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

    public VerbController getVerbController() {
        return verbController;
    }

    public void setVerbController(VerbController verbController) {
        this.verbController = verbController;
    }

    public RoomController getRoomController() {
        return roomController;
    }

    public void setRoomController(RoomController roomController) {
        this.roomController = roomController;
    }
}
