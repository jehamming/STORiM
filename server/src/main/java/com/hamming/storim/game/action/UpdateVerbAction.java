package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.DTOFactory;
import com.hamming.storim.factories.VerbFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.Verb;
import com.hamming.storim.model.dto.VerbDto;
import com.hamming.storim.model.dto.protocol.verb.GetVerbResultDTO;
import com.hamming.storim.model.dto.protocol.verb.UpdateVerbDto;

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
