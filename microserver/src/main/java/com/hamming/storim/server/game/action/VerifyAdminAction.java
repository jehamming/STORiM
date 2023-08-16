package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.VerifyAdminRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.VerifyAdminResponseDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
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
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        String adminPassword = getDto().getAdminPassword();
        VerifyAdminResponseDTO responseDTO;
        boolean success = false;
        String errorMessage = null;
        // Verify Admin with UserDataServer
        try {
            client.getServer().getUserDataServerProxy().verifyAdmin(adminPassword);
            client.setAdmin(true);
            success = true;
        } catch (STORIMException e) {
            client.setAdmin(false);
            errorMessage = e.getMessage();
        }
        responseDTO = new VerifyAdminResponseDTO(success, errorMessage);
        getClient().send(responseDTO);
    }
}
