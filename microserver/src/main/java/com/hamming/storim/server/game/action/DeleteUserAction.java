package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.DeleteUserDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

public class DeleteUserAction extends Action<DeleteUserDto> {
    private GameController controller;


    public DeleteUserAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        client.getServer().getUserDataServerProxy().deleteUser(getDto());
    }

}
