package com.hamming.storim.server.engine.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.AddUserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.AddUserResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.engine.GameController;

public class AddUserAction extends Action<AddUserDto> {
    private GameController controller;

    public AddUserAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        boolean success = false;
        String error = "";
        UserDto newUSer = null;
        try {
            newUSer = client.getServer().getUserDataServerProxy().addUser(getDto());
            success = true;

        } catch (STORIMException e) {
            error = e.getMessage();
        }
        AddUserResultDTO resultDTO = new AddUserResultDTO(success, error, newUSer);
        client.send(resultDTO);
    }
}
