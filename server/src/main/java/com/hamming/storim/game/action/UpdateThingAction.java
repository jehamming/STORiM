package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.User;
import com.hamming.storim.model.dto.protocol.thing.AddThingDto;
import com.hamming.storim.model.dto.protocol.thing.UpdateThingDto;
import com.hamming.storim.util.ImageUtils;

import java.awt.*;

public class UpdateThingAction extends Action<UpdateThingDto> {
    private GameController gameController;
    private ClientConnection client;

    public UpdateThingAction(GameController controller, ClientConnection client) {
        this.gameController = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        UpdateThingDto dto = getDto();
        Image image = ImageUtils.decode(dto.getImageData());
        gameController.updateThing(dto.getId(), dto.getName(), dto.getDescription(), dto.getScale(), dto.getRotation(), image);
    }

}
