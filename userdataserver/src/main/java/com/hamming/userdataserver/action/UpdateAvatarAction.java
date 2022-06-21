package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.UpdateAvatarRequestDto;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.UpdateAvatarResponseDTO;
import com.hamming.userdataserver.DTOFactory;
import com.hamming.userdataserver.factories.AvatarFactory;
import com.hamming.userdataserver.model.Avatar;

public class UpdateAvatarAction extends Action<UpdateAvatarRequestDto> {
    private ServerWorker serverWorker;


    public UpdateAvatarAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        UpdateAvatarRequestDto dto = getDto();
        String errorMessage = "";
        AvatarDto avatarDto = null;
        boolean success = false;

        Long avatarId = dto.getAvatarId();
        Avatar avatar = AvatarFactory.getInstance().findAvatarById(avatarId);
        if (avatar != null) {
            avatar.setName(dto.getName());
            avatar.setImage(ImageUtils.decode(dto.getImageData()));
            AvatarFactory.getInstance().updateAvatar(avatar);
            avatarDto = DTOFactory.getInstance().getAvatarDTO(avatar);
            success = true;
        } else {
            errorMessage = dto.getClass().getSimpleName() + ": Avatar " + avatarId + " not found!";
        }
        UpdateAvatarResponseDTO reponse = new UpdateAvatarResponseDTO(success, errorMessage, avatarDto);
        getClient().send(reponse);
    }

}
