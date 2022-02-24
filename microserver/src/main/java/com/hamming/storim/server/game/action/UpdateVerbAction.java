package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateVerbDto;
import com.hamming.storim.common.dto.protocol.serverpush.VerbUpdatedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.UpdateVerbResponseDTO;
import com.hamming.storim.server.game.GameController;

public class UpdateVerbAction extends Action<UpdateVerbDto> {
    private GameController controller;


    public UpdateVerbAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UpdateVerbDto dto = getDto();

        VerbDto verbDto = client.getServer().getUserDataServerProxy().updateVerb(dto.getVerbId(), dto.getName(), dto.getToCaller(), dto.getToLocation());
        if ( verbDto != null) {
            VerbUpdatedDTO verbUpdatedDTO = new VerbUpdatedDTO(verbDto);
            getClient().send(verbUpdatedDTO);
        }

    }

}
