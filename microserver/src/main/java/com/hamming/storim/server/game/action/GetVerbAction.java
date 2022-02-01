package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.VerbDetailsDTO;
import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetVerbDetailsRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetVerbDetailsResponseDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.VerbFactory;
import com.hamming.storim.server.common.model.Verb;
import com.hamming.storim.server.game.GameController;

public class GetVerbAction extends Action<GetVerbDetailsRequestDTO> {
    private GameController controller;


    public GetVerbAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        boolean success = false;
        String errorMessage = "";
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        VerbDetailsDTO verbDetailsDTO = client.getServer().getDataServerConnection().getVerb(getDto().getVerbID());
        if ( verbDetailsDTO != null ) {
            success  = true;
        } else {
            errorMessage = "Verb " +getDto().getVerbID()+" not found (??) ";
        }

        GetVerbDetailsResponseDTO reponse = new GetVerbDetailsResponseDTO(success, errorMessage, verbDetailsDTO);
        getClient().send(reponse);
    }

}
