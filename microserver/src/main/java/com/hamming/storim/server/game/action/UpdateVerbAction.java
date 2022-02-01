package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.UpdateVerbDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

public class UpdateVerbAction extends Action<UpdateVerbDto> {
    private GameController controller;


    public UpdateVerbAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        //FIXME
//        UpdateVerbDto dto = getDto();
//        Verb verb = VerbFactory.getInstance().updateVerb(dto.getVerbId(), dto.getName(), dto.getToCaller(), dto.getToLocation());
//        if ( verb != null ) {
//            VerbDto verbDto = DTOFactory.getInstance().getVerbDto(verb);
//            GetVerbDetailsResultDTO getCommandResultDTO = DTOFactory.getInstance().getVerbResultDto(true, null, verbDto);
//            getClient().send(getCommandResultDTO);
//        }
    }

}
