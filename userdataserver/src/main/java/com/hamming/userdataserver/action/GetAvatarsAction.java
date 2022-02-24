package com.hamming.userdataserver.action;

import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.GetAvatarsRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.GetAvatarsResponseDTO;
import com.hamming.userdataserver.factories.AvatarFactory;
import com.hamming.userdataserver.model.Avatar;
import com.hamming.userdataserver.model.User;
import com.hamming.userdataserver.factories.UserFactory;

import java.util.ArrayList;
import java.util.List;

public class GetAvatarsAction extends Action<GetAvatarsRequestDTO> {

    private ServerWorker serverWorker;

    public GetAvatarsAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        Long userId = getDto().getUserId();
        boolean success = false;
        String message = null;
        List<Long> avatarIds = new ArrayList<>();

        User user = UserFactory.getInstance().findUserById(userId);
        if (user != null) {
            success = true;
            List<Avatar> avatars = AvatarFactory.getInstance().getAvatars(user);
            for (Avatar avatar : avatars) {
                avatarIds.add(avatar.getId());
            }
        } else {
            message = "User " + userId + " not found!";
        }


        GetAvatarsResponseDTO response = new GetAvatarsResponseDTO(success, message, avatarIds);
        getClient().send(response);
    }

}
