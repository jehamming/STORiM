package com.hamming.userdataserver.action;

import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.DeleteVerbRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.DeleteVerbResponseDTO;
import com.hamming.userdataserver.factories.VerbFactory;

public class DeleteVerbAction extends Action<DeleteVerbRequestDto> {
    private ServerWorker serverWorker;


    public DeleteVerbAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        Long verbId = getDto().getVerbId();
        String errorMessage = "";
        boolean success = false;

        success = VerbFactory.getInstance().deleteVerb(verbId);

        if ( !success ) {
            errorMessage = "Delete Verb failed";
        }

        DeleteVerbResponseDTO responseDTO = new DeleteVerbResponseDTO(success, errorMessage);
        getClient().send(responseDTO);
    }

}
