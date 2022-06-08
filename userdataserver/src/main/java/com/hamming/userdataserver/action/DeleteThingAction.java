package com.hamming.userdataserver.action;

import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.DeleteThingRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.DeleteThingResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.DeleteVerbRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.DeleteVerbResponseDTO;
import com.hamming.userdataserver.factories.ThingFactory;
import com.hamming.userdataserver.factories.VerbFactory;

public class DeleteThingAction extends Action<DeleteThingRequestDto> {
    private ServerWorker serverWorker;

    public DeleteThingAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        Long thingId = getDto().getThingID();
        String errorMessage = "";
        boolean success = false;

        success = ThingFactory.getInstance().deleteThing(thingId);

        if ( !success ) {
            errorMessage = "Delete Thing failed";
        }

        DeleteThingResponseDTO responseDTO = new DeleteThingResponseDTO(success, errorMessage);
        getClient().send(responseDTO);
    }

}
