package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.VerbDetailsDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetVerbResponseDTO;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.GetVerbDetailsRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.GetVerbDetailsResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.VerbFactory;
import com.hamming.userdataserver.model.Verb;

public class GetVerbAction extends Action<GetVerbDetailsRequestDTO> {

    private ServerWorker serverWorker;

    public GetVerbAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        Long verbId = getDto().getVerbID();
        VerbDetailsDTO verbDetailsDTO = null;
        boolean success = false;
        String errorMessage = "";

        Verb verb  = VerbFactory.getInstance().findVerbByID(verbId);
        if ( verb != null ) {
            verbDetailsDTO = DTOFactory.getInstance().getVerbDetailsDto(verb);
            success = true;
        } else {
            errorMessage = "Verb " + verbId + " not found!";
        }
        GetVerbDetailsResponseDTO getVerbDetailsResponseDTO = new GetVerbDetailsResponseDTO(success, errorMessage, verbDetailsDTO);
        getClient().send(getVerbDetailsResponseDTO);
    }
}