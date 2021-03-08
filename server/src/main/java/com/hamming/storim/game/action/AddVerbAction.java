package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.VerbFactory;
import com.hamming.storim.factories.DTOFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.User;
import com.hamming.storim.model.Verb;
import com.hamming.storim.model.dto.VerbDto;
import com.hamming.storim.model.dto.protocol.AddVerbDto;
import com.hamming.storim.model.dto.protocol.GetVerbResultDTO;

public class AddVerbAction extends Action<AddVerbDto> {
    private GameController controller;
    private ClientConnection client;

    public AddVerbAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        AddVerbDto dto = getDto();
        User creator = client.getCurrentUser();
        Verb verb = VerbFactory.getInstance().createVerb(creator, dto.getName(), dto.getShortName(), dto.getToCaller(), dto.getToLocation());
        if ( verb != null ) {
            client.getCurrentUser().addVerb(verb);
            VerbDto verbDto = DTOFactory.getInstance().getVerbDto(verb);
            GetVerbResultDTO getCommandResultDTO = DTOFactory.getInstance().getVerbResultDto(true, null, verbDto);
            client.send(getCommandResultDTO);
        }
    }

}
