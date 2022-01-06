package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.verb.GetVerbDTO;
import com.hamming.storim.common.dto.protocol.verb.GetVerbResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.VerbFactory;
import com.hamming.storim.server.common.model.Verb;
import com.hamming.storim.server.game.GameController;

public class GetVerbAction extends Action<GetVerbDTO> {
    private GameController controller;
    private STORIMClientConnection client;

    public GetVerbAction(GameController controller, STORIMClientConnection client) {

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
