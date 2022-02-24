package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.AddVerbDto;
import com.hamming.storim.common.dto.protocol.serverpush.VerbAddedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.AddVerbResponseDTO;
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
        UserDto creator = client.getCurrentUser();

        VerbDto verbDto = client.getServer().getUserDataServerProxy().addVerb(creator, dto.getName(), dto.getToCaller(), dto.getToLocation());
        if (verbDto!= null ) {
            VerbAddedDTO verbAddedDTO = new VerbAddedDTO(verbDto);
            getClient().send(verbAddedDTO);
        }

    }

}
