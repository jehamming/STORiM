package nl.hamming.storimapp.engine.actions;

import com.hamming.storim.model.dto.ThingDto;
import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Player;
import nl.hamming.storimapp.view.Thing;

import java.awt.*;


public class AddThingAction implements Action {

    private GameView viewer;
    private ThingDto thingDto;

    public AddThingAction(GameView viewer, ThingDto thingDto) {
        this.viewer = viewer;
        this.thingDto = thingDto;
    }

    @Override
    public void execute() {
        Thing thing = new Thing(thingDto.getId());
        thing.setImage(thingDto.getImage());
        thing.setScale(thingDto.getScale());
        thing.setRotation(thingDto.getRotation());
        thing.setX(thingDto.getLocation().getX());
        thing.setY(thingDto.getLocation().getY());
        viewer.addThing(thing);

    }

    @Override
    public String toString() {
        return "AddThingAction{" +
                "thingDto=" + thingDto +
                '}';
    }
}
