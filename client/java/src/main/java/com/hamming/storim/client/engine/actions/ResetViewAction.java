package com.hamming.storim.client.engine.actions;

import com.hamming.storim.client.view.GameView;

import java.util.ArrayList;


public class ResetViewAction implements Action {

    private GameView viewer;


    public ResetViewAction(GameView viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        viewer.setTile(null);
        viewer.setRoom(null);
        viewer.setPlayers(new ArrayList<>());
        viewer.setThing(new ArrayList<>());
    }

    @Override
    public String toString() {
        return "ResetViewAction{" +
                '}';
    }
}
