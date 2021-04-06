package com.hamming.storim.client.engine.actions;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.client.view.GameView;
import com.hamming.storim.client.view.Thing;


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
            thing.setImage(ImageUtils.decode(thingDto.getImageData()));
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
