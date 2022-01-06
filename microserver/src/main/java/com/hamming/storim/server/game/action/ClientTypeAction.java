package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.ClientTypeDTO;
import com.hamming.storim.common.dto.protocol.login.ConnectRequestDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

public class ClientTypeAction extends Action<ClientTypeDTO> {
    private GameController controller;
    private STORIMClientConnection client;


    public ClientTypeAction(GameController controller, STORIMClientConnection client) {

        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        client.setClientID(getDto().getName());
    }

}
