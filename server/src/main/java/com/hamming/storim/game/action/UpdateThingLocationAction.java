package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.ThingFactory;
import com.hamming.storim.factories.TileFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.Thing;
import com.hamming.storim.model.dto.protocol.thing.UpdateThingDto;
import com.hamming.storim.model.dto.protocol.thing.UpdateThingLocationDto;
import com.hamming.storim.util.ImageUtils;

import java.awt.*;

public class UpdateThingLocationAction extends Action<UpdateThingLocationDto> {
    private GameController gameController;
    private ClientConnection client;

    public UpdateThingLocationAction(GameController controller, ClientConnection client) {
        this.gameController = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        UpdateThingLocationDto dto = getDto();
        Thing thing = ThingFactory.getInstance().findThingById(dto.getId());
        if ( thing != null ) {
            gameController.updateThingLocation(thing, dto.getX(), dto.getY());
        }
    }

}
