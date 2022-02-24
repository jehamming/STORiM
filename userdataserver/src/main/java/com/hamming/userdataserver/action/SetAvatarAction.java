package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.SetAvatarRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.SetAvatarResponseDto;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.AvatarFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.Avatar;
import com.hamming.userdataserver.model.User;

public class SetAvatarAction extends Action<SetAvatarRequestDto> {
    private ServerWorker serverWorker;

    public SetAvatarAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        SetAvatarRequestDto dto = getDto();
        String errorMessage = "";
        AvatarDto avatarDto = null;
        boolean success = false;

        Long userId = getDto().getUserId();
        Long avatarId = getDto().getAvatarId();
        User user =  UserFactory.getInstance().findUserById(userId);
        Avatar avatar = AvatarFactory.getInstance().findAvatarById(avatarId);
        if (user != null && avatar != null) {
            user.setCurrentAvatar(avatar);
            avatarDto = DTOFactory.getInstance().getAvatarDTO(avatar);
            success = true;
        } else {
            errorMessage = dto.getClass().getSimpleName() + ": User " + userId + " or Avatar " + avatarId + " not found!";
        }
        SetAvatarResponseDto setAvatarResponseDto = new SetAvatarResponseDto(success, errorMessage, avatarDto);
        getClient().send(setAvatarResponseDto);
    }

}
