package com.hamming.storim.client.engine.actions;

import com.hamming.storim.client.view.GameView;
import com.hamming.storim.client.view.Thing;

public class DeleteThingAction implements Action {

    private GameView viewer;
    private Long thingId;

    public DeleteThingAction(GameView viewer, Long thingId) {
        this.viewer = viewer;
        this.thingId = thingId;
    }

    @Override
    public void execute() {
        Thing t = viewer.getThing(thingId);
        if ( t != null ) {
            viewer.deleteThing(t);
        }
    }

    @Override
    public String toString() {
        return "DeleteAvatarAction{" +
                "userId=" + thingId +
                '}';
    }
}
