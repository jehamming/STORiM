package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.AddAvatarRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.AddAvatarResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.AvatarFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.model.Avatar;
import com.hamming.userdataserver.model.User;

public class AddAvatarAction extends Action<AddAvatarRequestDto> {
    private ServerWorker serverWorker;


    public AddAvatarAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        AddAvatarRequestDto dto = getDto();
        String errorMessage = "";
        AvatarDto avatarDto = null;
        boolean success = false;

        Long creatorId = getDto().getUserId();
        User creator =  UserFactory.getInstance().findUserById(creatorId);
        if (creator != null) {
            Avatar avatar = AvatarFactory.getInstance().createAvatar(creatorId, dto.getName(), ImageUtils.decode(dto.getImageData()));
            avatarDto = DTOFactory.getInstance().getAvatarDTO(avatar);
            success = true;
        } else {
            errorMessage = dto.getClass().getSimpleName() + ": Avatar " + creatorId + " not found!";
        }
        AddAvatarResponseDTO addAvatarResponseDTO = new AddAvatarResponseDTO(success, errorMessage, avatarDto);
        getClient().send(addAvatarResponseDTO);
    }

}
