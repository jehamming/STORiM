package nl.hamming.storimapp.engine.actions;

import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Thing;

import java.awt.*;


public class UpdateThingAction implements Action {

    private GameView viewer;
    private Long thingId;
    private Image image;
    private float scale;
    private int rotation;

    public UpdateThingAction(GameView viewer, Long thingID, Image image, float scale, int rotation) {
        this.viewer = viewer;
        this.thingId = thingID;
        this.image = image;
        this.scale = scale;
        this.rotation = rotation;
    }

    @Override
    public void execute() {
        Thing thing = viewer.getThing(thingId);
        if ( thing != null ) {
            thing.setImage(image);
            thing.setScale(scale);
            thing.setRotation(rotation);
        }
    }

    @Override
    public String toString() {
        return "UpdateThingAction{" +
                "thingId=" + thingId +
                ", scale=" + scale +
                ", rotation=" + rotation +
                '}';
    }
}
