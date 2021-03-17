package nl.hamming.storimapp.engine.actions;

import com.hamming.storim.model.dto.ThingDto;
import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Player;
import nl.hamming.storimapp.view.Thing;

import java.awt.*;


public class AddThingAction implements Action {

    private GameView viewer;
    private Long thingId;
    private Image image;
    private float scale, rotation;

    public AddThingAction(GameView viewer, Long thingID, Image image, float scale, float rotation) {
        this.viewer = viewer;
        this.thingId = thingID;
        this.image = image;
        this.scale = scale;
        this.rotation = rotation;
    }

    @Override
    public void execute() {
        Thing thing = new Thing(thingId);
        thing.setImage(image);
        thing.setScale(scale);
        thing.setRotation(rotation);
        viewer.addThing(thing);

    }

    @Override
    public String toString() {
        return "AddThingAction{" +
                "thingId=" + thingId +
                ", scale=" + scale +
                ", rotation=" + rotation +
                '}';
    }
}
