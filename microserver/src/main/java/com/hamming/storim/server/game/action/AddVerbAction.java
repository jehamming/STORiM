package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.AddVerbDto;
import com.hamming.storim.common.dto.protocol.serverpush.VerbAddedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.AddVerbResponseDTO;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.game.GameController;

public class AddVerbAction extends Action<AddVerbDto> {
    private GameController controller;


    public AddVerbAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        AddVerbDto dto = getDto();
        User creator = client.getCurrentUser();

        AddVerbResponseDTO response = client.getServer().getDataServerConnection().addVerb(creator, dto.getName(), dto.getToCaller(), dto.getToLocation());
        if (response.isSuccess()) {
            VerbAddedDTO verbAddedDTO = new VerbAddedDTO(response.getVerb());
            getClient().send(verbAddedDTO);
        } else {
            ErrorDTO errorDTO = new ErrorDTO("AddVerb", response.getErrorMessage());
            getClient().send(errorDTO);
        }

    }

}
