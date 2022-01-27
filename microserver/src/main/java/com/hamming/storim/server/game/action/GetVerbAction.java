package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetVerbDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetVerbResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.dto.DTOFactory;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.VerbFactory;
import com.hamming.storim.server.common.model.Verb;
import com.hamming.storim.server.game.GameController;

public class GetVerbAction extends Action<GetVerbDTO> {
    private GameController controller;


    public GetVerbAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;

    }

    @Override
    public void execute() {
        Verb cmd = VerbFactory.getInstance().findVerbByID(getDto().getCommandID());
        if ( cmd != null ) {
            VerbDto verbDto = DTOFactory.getInstance().getVerbDto(cmd);
            GetVerbResultDTO getCommandResultDTO = DTOFactory.getInstance().getVerbResultDto(true, null, verbDto);
            getClient().send(getCommandResultDTO);
        } else {
            GetVerbResultDTO getCommandResultDTO = DTOFactory.getInstance().getVerbResultDto(false, "Command "+ getDto().getCommandID() +" not found!", null);
            getClient().send(getCommandResultDTO);
        }
    }

}
