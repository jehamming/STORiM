package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.game.GameController;
import com.hamming.storim.game.Protocol;
import com.hamming.storim.model.dto.protocol.VersionCheckDTO;

public class ProtocolVersionAction extends Action<VersionCheckDTO> {
    private GameController controller;
    private ClientConnection client;

    public ProtocolVersionAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        if (Protocol.version.equals(getDto().getClientVersion())) {
            getDto().setVersionCompatible(true);
            client.getProtocolHandler().protocolVersionCompatible();
        } else {
            getDto().setVersionCompatible(false);
            getDto().setServerVersion(Protocol.version);
        }
        client.send(getDto());
    }


}
