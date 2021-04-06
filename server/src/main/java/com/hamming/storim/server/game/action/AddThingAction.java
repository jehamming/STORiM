package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.User;
import com.hamming.storim.common.dto.protocol.thing.AddThingDto;
import com.hamming.storim.server.ImageUtils;

import java.awt.*;

public class AddThingAction extends Action<AddThingDto> {
    private GameController gameController;
    private ClientConnection client;

    public AddThingAction(GameController controller, ClientConnection client) {
        this.gameController = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        AddThingDto dto = getDto();
        User creator = client.getCurrentUser();

        Image image = ImageUtils.decode(dto.getImageData());
        gameController.addThing(creator, dto.getName(), dto.getDescription(), dto.getScale(), dto.getRotation(), image);
    }

}
