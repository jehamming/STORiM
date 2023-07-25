package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.requestresponse.AddUserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.AddUserResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

public class AddUserAction extends Action<AddUserDto> {
    private GameController controller;

    public AddUserAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        AddUserResultDTO resultDTO = client.getServer().getUserDataServerProxy().addUser(getDto());
        client.send(resultDTO);
    }

}
