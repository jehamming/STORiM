package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.AddUserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.AddUserResultDTO;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.AddVerbResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.factories.VerbFactory;
import com.hamming.userdataserver.model.User;
import com.hamming.userdataserver.model.Verb;

public class AddUserAction extends Action<AddUserDto> {
    private ServerWorker serverWorker;


    public AddUserAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        AddUserDto dto = getDto();
        String errorMessage = "";
        UserDto userDto = null;
        boolean success = false;


        User user = UserFactory.getInstance().findUserByUsername(dto.getUsername());
        if ( user != null ) {
            errorMessage = dto.getClass().getSimpleName() + ": username " + dto.getUsername() + " already in use!";
        } else {
            User root = UserFactory.getInstance().getRootUser();
            user = UserFactory.getInstance().addUser(root, dto.getName(), dto.getUsername(), dto.getPassword(), dto.getEmail());
            userDto = DTOFactory.getInstance().getUserDTO(user);
            //Create a default verb
            Verb cmdSayPlayer1 = VerbFactory.getInstance().createVerb("Say", user);
            cmdSayPlayer1.setToCaller("You say '${message}'");
            cmdSayPlayer1.setToLocation("${caller} says '${message}'");
            success = true;
        }
        AddUserResultDTO addUserResultDTO = new AddUserResultDTO(success, errorMessage, userDto);
        getClient().send(addUserResultDTO);
    }

}
