package com.hamming.userdataserver.action;

import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.DeleteAvatarRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.DeleteAvatarResponseDTO;
import com.hamming.userdataserver.factories.AvatarFactory;
import com.hamming.userdataserver.model.Avatar;

public class DeleteAvatarAction extends Action<DeleteAvatarRequestDTO> {
    private ServerWorker serverWorker;


    public DeleteAvatarAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        DeleteAvatarRequestDTO dto = getDto();
        String errorMessage = "";
        boolean success = false;

        Long avatarID = dto.getAvatarID();
        Avatar avatar = AvatarFactory.getInstance().findAvatarById(avatarID);
        if ( avatar != null ) {
            success = true;
            AvatarFactory.getInstance().deleteAvatar(avatar);
        } else {
            errorMessage = dto.getClass().getSimpleName() + ": Avatar " + avatarID + " not found!";
        }

        DeleteAvatarResponseDTO response = new DeleteAvatarResponseDTO(success, errorMessage);
        getClient().send(response);
    }

}
