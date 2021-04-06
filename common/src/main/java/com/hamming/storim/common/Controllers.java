package com.hamming.storim.common;

import com.hamming.storim.common.controllers.*;
import com.hamming.storim.common.interfaces.ViewerController;

public class Controllers {


    private ConnectionController connectionController;
    private UserController userController;
    private VerbController verbController;
    private RoomController roomController;
    private ThingController thingController;
    private ViewerController viewerController;

    public ConnectionController getConnectionController() {
        return connectionController;
    }

    public void setConnectionController(ConnectionController connectionController) {
        this.connectionController = connectionController;
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

    public ThingController getThingController() {
        return thingController;
    }

    public void setThingController(ThingController thingController) {
        this.thingController = thingController;
    }

    public ViewerController getViewerController() {
        return viewerController;
    }

    public void setViewerController(ViewerController viewerController) {
        this.viewerController = viewerController;
    }
}
