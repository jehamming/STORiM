package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.DeleteVerbDTO;
import com.hamming.storim.common.dto.protocol.serverpush.VerbDeletedDTO;
import com.hamming.storim.server.STORIMClientConnection;
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

        DeleteVerbResponseDTO response = client.getServer().getDataServerConnection().deleteVerb(dto.getVerbID());
        if (response.isSuccess()) {
            VerbDeletedDTO verbDeletedDTO = new VerbDeletedDTO(dto.getVerbID());
            getClient().send(verbDeletedDTO);
        } else {
            ErrorDTO errorDTO = new ErrorDTO("DeleteVerb", response.getErrorMessage());
            getClient().send(errorDTO);
        }

    }

}
