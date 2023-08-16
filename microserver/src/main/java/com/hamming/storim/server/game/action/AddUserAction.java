package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.AddUserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.AddUserResultDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;

public class AddUserAction extends Action<AddUserDto> {
    private GameController controller;

    public AddUserAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        try {
        UserDto newUSer = client.getServer().getUserDataServerProxy().addUser(getDto());
        AddUserResultDTO resultDTO = new AddUserResultDTO(true, null, newUSer);
        client.send(resultDTO);
        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), e.getMessage());
            client.send(errorDTO);
        }
    }

}
