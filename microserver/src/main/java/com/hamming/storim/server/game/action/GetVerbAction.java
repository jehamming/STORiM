package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.VerbDetailsDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetVerbDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetVerbResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.GetVerbDetailsRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.GetVerbDetailsResponseDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

public class GetVerbAction extends Action<GetVerbDTO> {
    private GameController controller;


    public GetVerbAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        VerbDetailsDTO verbDetailsDTO = client.getServer().getUserDataServerProxy().getVerb(getDto().getVerbID());
        GetVerbResponseDTO response = new GetVerbResponseDTO(verbDetailsDTO);
        getClient().send(response);
    }

}
