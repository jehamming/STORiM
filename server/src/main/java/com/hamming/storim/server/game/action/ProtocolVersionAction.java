package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.common.Protocol;
import com.hamming.storim.common.dto.protocol.VersionCheckDTO;

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
