package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.AddAvatarDto;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarAddedDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.AddAvatarResponseDTO;
import com.hamming.storim.server.game.GameController;

public class AddAvatarAction extends Action<AddAvatarDto> {

    public AddAvatarAction(STORIMClientConnection client) {
        super(client);
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        AddAvatarDto dto = getDto();
        UserDto creator = client.getCurrentUser();
        if (dto.getImageData() != null ) {
            AddAvatarResponseDTO response = client.getServer().getDataServerConnection().addAvatar(creator, dto.getName(), dto.getImageData());
            if (response.isSuccess()) {
                AvatarAddedDTO avatarAddedDTO = new AvatarAddedDTO(response.getAvatar());
                getClient().send(avatarAddedDTO);
            } else {
                ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), response.getErrorMessage());
                getClient().send(errorDTO);
            }
        } else {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "No Imagedata!");
            getClient().send(errorDTO);
        }
    }

}
