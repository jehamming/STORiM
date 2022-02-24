package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.AddVerbRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.AddVerbResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.factories.VerbFactory;
import com.hamming.userdataserver.model.User;
import com.hamming.userdataserver.model.Verb;

public class AddVerbAction extends Action<AddVerbRequestDto> {
    private ServerWorker serverWorker;


    public AddVerbAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        AddVerbRequestDto dto = getDto();
        String errorMessage = "";
        VerbDto verbDto = null;
        boolean success = false;
        Long userID = getDto().getUserId();
        User creator =  UserFactory.getInstance().findUserById(userID);
        if (creator != null) {
            Verb verb = VerbFactory.getInstance().createVerb(creator, dto.getName(), dto.getToCaller(), dto.getToLocation());
            verbDto = DTOFactory.getInstance().getVerbDto(verb);
            success = true;
        } else {
            errorMessage = dto.getClass().getSimpleName() + ": User " + userID + " not found!";
        }
        AddVerbResponseDTO addVerbResponseDTO = new AddVerbResponseDTO(success, errorMessage, verbDto);
        getClient().send(addVerbResponseDTO);
    }

}
