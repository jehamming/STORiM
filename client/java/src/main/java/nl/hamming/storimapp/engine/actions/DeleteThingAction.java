package nl.hamming.storimapp.engine.actions;

import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Player;
import nl.hamming.storimapp.view.Thing;

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
