package nl.hamming.storimapp.engine.actions;

import com.hamming.storim.model.dto.ThingDto;
import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Thing;

import java.awt.*;


public class UpdateThingAction implements Action {

    private GameView viewer;
    private ThingDto thingDto;

    public UpdateThingAction(GameView viewer, ThingDto thingDto) {
        this.viewer = viewer;
        this.thingDto = thingDto;
    }

    @Override
    public void execute() {
        Thing thing = viewer.getThing(thingDto.getId());
        if ( thing != null ) {
            thing.setImage(thingDto.getImage());
            thing.setScale(thingDto.getScale());
            thing.setRotation(thingDto.getRotation());
            thing.setX(thingDto.getLocation().getX());
            thing.setY(thingDto.getLocation().getY());
        }
    }


    @Override
    public String
    toString() {
        return "UpdateThingAction{" +
                ", thingDto=" + thingDto +
                '}';
    }
}
