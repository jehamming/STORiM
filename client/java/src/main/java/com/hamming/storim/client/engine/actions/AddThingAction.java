package com.hamming.storim.client.engine.actions;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.client.view.GameView;
import com.hamming.storim.client.view.Thing;
import com.hamming.storim.common.dto.ThingDto;


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
        thing.setImage(ImageUtils.decode(thingDto.getImageData()));
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
