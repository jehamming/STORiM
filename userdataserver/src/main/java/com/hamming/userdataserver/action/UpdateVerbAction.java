package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.UpdateVerbRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.UpdateVerbResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.VerbFactory;
import com.hamming.userdataserver.model.Verb;

public class UpdateVerbAction extends Action<UpdateVerbRequestDto> {
    private ServerWorker serverWorker;


    public UpdateVerbAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        UpdateVerbRequestDto dto = getDto();
        String errorMessage = "";
        VerbDto verbDto = null;
        boolean success = false;

        Verb verb = VerbFactory.getInstance().findVerbByID(dto.getVerbId());
        if ( verb != null ) {
                verb.setName(dto.getName());
                verb.setToCaller(dto.getToCaller());
                verb.setToLocation(dto.getToLocation());
                verbDto = DTOFactory.getInstance().getVerbDto(verb);
                success = true;
        } else {
            errorMessage = "Verb " + dto.getVerbId() + " not found!";
        }

        UpdateVerbResponseDTO updateVerbResponseDTO = new UpdateVerbResponseDTO(success, errorMessage, verbDto);
        getClient().send(updateVerbResponseDTO);
    }

}
