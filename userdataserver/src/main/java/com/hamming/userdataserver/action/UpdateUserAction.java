package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.requestresponse.UpdateUserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.UpdateUserResultDTO;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.UpdateVerbRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.UpdateVerbResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.factories.VerbFactory;
import com.hamming.userdataserver.model.User;
import com.hamming.userdataserver.model.Verb;

public class UpdateUserAction extends Action<UpdateUserDto> {
    private ServerWorker serverWorker;


    public UpdateUserAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        UpdateUserDto dto = getDto();
        String errorMessage = "";
        UserDto userDto = null;
        boolean success = false;


        User user = UserFactory.getInstance().findUserById(dto.getId());
        if ( user != null ) {

            if ( dto.getName() != null ) user.setName(dto.getName());
            if ( dto.getPassword() != null ) user.setPassword(dto.getPassword());
            if ( dto.getEmail() != null ) user.setEmail(dto.getEmail());
            if ( dto.getUsername() != null ) user.setUsername(dto.getUsername());

            success = true;
            userDto = DTOFactory.getInstance().getUserDTO(user);
        } else {
            errorMessage = getClass().getSimpleName() + "- User "+ dto.getId() + " not found";
        }

        UpdateUserResultDTO resultDTO = new UpdateUserResultDTO(success, errorMessage, userDto);
        getClient().send(resultDTO);
    }

}
