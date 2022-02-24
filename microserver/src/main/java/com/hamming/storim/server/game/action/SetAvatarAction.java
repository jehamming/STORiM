package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.SetAvatarDto;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarSetDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.SetAvatarResponseDto;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.RoomEvent;

public class SetAvatarAction extends Action<SetAvatarDto> {
    private GameController gameController;

    public SetAvatarAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.gameController = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        SetAvatarDto dto = getDto();
        UserDto currentUser = client.getCurrentUser();

        AvatarDto avatarDto = client.getServer().getUserDataServerProxy().setAvatar(dto.getAvatarId(), currentUser.getId());

        if (avatarDto != null) {
            currentUser.setCurrentAvatarID(avatarDto.getId());
            AvatarSetDTO avatarSetDTO = new AvatarSetDTO(currentUser.getId(), avatarDto);
            getClient().send(avatarSetDTO);
            avatarSet(client, currentUser,avatarDto );
        }
    }


    public void avatarSet(ClientConnection source, UserDto user, AvatarDto avatar) {
        Location location = gameController.getGameState().getLocation(user.getId());
        gameController.fireRoomEvent(source, location.getRoom().getId(), new RoomEvent(RoomEvent.Type.AVATARSET, user, avatar));
    }

}
