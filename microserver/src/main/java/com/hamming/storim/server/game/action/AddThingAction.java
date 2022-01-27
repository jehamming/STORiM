package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.requestresponse.AddThingDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.game.GameController;

import java.awt.*;

public class AddThingAction extends Action<AddThingDto> {
    private GameController gameController;


    public AddThingAction(GameController controller, STORIMClientConnection client) {
        super(client); this.gameController = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        AddThingDto dto = getDto();
        User creator = client.getCurrentUser();

        Image image = ImageUtils.decode(dto.getImageData());
        gameController.addThing(getClient(), creator.getId(), dto.getName(), dto.getDescription(), dto.getScale(), dto.getRotation(), image);
    }

}
