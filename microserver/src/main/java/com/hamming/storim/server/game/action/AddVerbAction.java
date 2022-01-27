package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.requestresponse.AddVerbDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetVerbResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.VerbFactory;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.common.model.Verb;
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
        Verb verb = VerbFactory.getInstance().createVerb(creator, dto.getName(), dto.getToCaller(), dto.getToLocation());
        if ( verb != null ) {
            VerbDto verbDto = DTOFactory.getInstance().getVerbDto(verb);
            GetVerbResultDTO getCommandResultDTO = DTOFactory.getInstance().getVerbResultDto(true, null, verbDto);
            getClient().send(getCommandResultDTO);
        }
    }

}
