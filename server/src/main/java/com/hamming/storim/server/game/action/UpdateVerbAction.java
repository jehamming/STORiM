package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.factories.DTOFactory;
import com.hamming.storim.server.factories.VerbFactory;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.Verb;
import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.verb.GetVerbResultDTO;
import com.hamming.storim.common.dto.protocol.verb.UpdateVerbDto;

public class UpdateVerbAction extends Action<UpdateVerbDto> {
    private GameController controller;
    private ClientConnection client;

    public UpdateVerbAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        UpdateVerbDto dto = getDto();
        Verb verb = VerbFactory.getInstance().updateVerb(dto.getVerbId(), dto.getName(), dto.getToCaller(), dto.getToLocation());
        if ( verb != null ) {
            VerbDto verbDto = DTOFactory.getInstance().getVerbDto(verb);
            GetVerbResultDTO getCommandResultDTO = DTOFactory.getInstance().getVerbResultDto(true, null, verbDto);
            client.send(getCommandResultDTO);
        }
    }

}
