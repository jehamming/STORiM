package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.DeleteVerbDTO;
import com.hamming.storim.common.dto.protocol.serverpush.VerbDeletedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.DeleteVerbResponseDTO;
import com.hamming.storim.server.game.GameController;

public class DeleteVerbAction extends Action<DeleteVerbDTO> {
    private GameController controller;

    public DeleteVerbAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        DeleteVerbDTO dto = getDto();
        try {
            client.getServer().getUserDataServerProxy().deleteVerb(dto.getVerbID());
            VerbDeletedDTO verbDeletedDTO = new VerbDeletedDTO(dto.getVerbID());
            getClient().send(verbDeletedDTO);
        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), e.getMessage());
            client.send(errorDTO);
        }


    }

}
