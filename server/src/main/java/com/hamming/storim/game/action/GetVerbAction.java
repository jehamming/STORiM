package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.VerbFactory;
import com.hamming.storim.factories.DTOFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.Verb;
import com.hamming.storim.model.dto.VerbDto;
import com.hamming.storim.model.dto.protocol.verb.GetVerbDTO;
import com.hamming.storim.model.dto.protocol.verb.GetVerbResultDTO;

public class GetVerbAction extends Action<GetVerbDTO> {
    private GameController controller;
    private ClientConnection client;

    public GetVerbAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        Verb cmd = VerbFactory.getInstance().findVerbByID(getDto().getCommandID());
        if ( cmd != null ) {
            VerbDto verbDto = DTOFactory.getInstance().getVerbDto(cmd);
            GetVerbResultDTO getCommandResultDTO = DTOFactory.getInstance().getVerbResultDto(true, null, verbDto);
            client.send(getCommandResultDTO);
        } else {
            GetVerbResultDTO getCommandResultDTO = DTOFactory.getInstance().getVerbResultDto(false, "Command "+ getDto().getCommandID() +" not found!", null);
            client.send(getCommandResultDTO);
        }
    }

}
