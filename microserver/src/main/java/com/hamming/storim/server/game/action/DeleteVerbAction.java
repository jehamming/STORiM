package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.AddVerbDto;
import com.hamming.storim.common.dto.protocol.requestresponse.DeleteVerbDTO;
import com.hamming.storim.common.dto.protocol.serverpush.VerbAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.VerbDeletedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.AddVerbResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.DeleteVerbResponseDTO;
import com.hamming.storim.server.common.factories.VerbFactory;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.common.model.Verb;
import com.hamming.storim.server.game.GameController;

public class DeleteVerbAction extends Action<DeleteVerbDTO> {
    private GameController controller;

    public DeleteVerbAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        Verb verb = VerbFactory.getInstance().findVerbByID(getDto().getVerbID());
        boolean success = VerbFactory.getInstance().deleteVerb(getDto().getVerbID());
        if (success) {
            controller.verbDeleted(getClient(), verb);
        }

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
