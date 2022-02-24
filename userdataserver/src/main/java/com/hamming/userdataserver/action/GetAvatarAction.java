package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.GetAvatarRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.GetAvatarResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.AvatarFactory;
import com.hamming.userdataserver.model.Avatar;

public class GetAvatarAction extends Action<GetAvatarRequestDTO> {

    private ServerWorker serverWorker;

    public GetAvatarAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        Long avatarId = getDto().getAvatarId();
        boolean success = false;
        String message = null;
        AvatarDto avatarDto = null;

        Avatar avatar = AvatarFactory.getInstance().findAvatarById(avatarId);
        if (avatar != null) {
            success = true;
            avatarDto = DTOFactory.getInstance().getAvatarDTO(avatar);
        } else {
            message = "Avatar " + avatarId + " not found!";
        }


        GetAvatarResponseDTO response = new GetAvatarResponseDTO(success, message, avatarDto);
        getClient().send(response);
    }

}
