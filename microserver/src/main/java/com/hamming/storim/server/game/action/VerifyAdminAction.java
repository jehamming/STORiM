package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.requestresponse.VerifyAdminRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.VerifyAdminResponseDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

public class VerifyAdminAction extends Action<VerifyAdminRequestDTO> {
    private GameController controller;

    public VerifyAdminAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        boolean success = false;
        String errorMessage = null;
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        String adminPassword = getDto().getAdminPassword();

        // Verify Admin with UserDataServer

        errorMessage = client.getServer().getUserDataServerProxy().verifyAdmin(adminPassword);

        if (errorMessage == null) {
            success = true;
        }
        VerifyAdminResponseDTO responseDTO = new VerifyAdminResponseDTO(success, errorMessage);
        client.setAdmin(success);
        getClient().send(responseDTO);
    }
}
