package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.thing.AddThingDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.game.GameController;

import java.awt.*;

public class AddThingAction extends Action<AddThingDto> {
    private GameController gameController;
    private STORIMClientConnection client;

    public AddThingAction(GameController controller, STORIMClientConnection client) {
        this.gameController = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        AddThingDto dto = getDto();
        User creator = client.getCurrentUser();

        Image image = ImageUtils.decode(dto.getImageData());
        gameController.addThing(creator.getId(), dto.getName(), dto.getDescription(), dto.getScale(), dto.getRotation(), image);
    }

}
