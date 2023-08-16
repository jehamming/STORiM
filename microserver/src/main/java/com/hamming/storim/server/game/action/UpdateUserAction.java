package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.STORIMException;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.common.dto.protocol.requestresponse.UpdateUserDto;

public class UpdateUserAction extends Action<UpdateUserDto> {
    private GameController controller;

    public UpdateUserAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        UpdateUserDto dto = getDto();
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        try {
            client.getServer().getUserDataServerProxy().updateUser(getDto());
        } catch (STORIMException e) {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), e.getMessage());
            client.send(errorDTO);
        }

    }

}
