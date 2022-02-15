package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.SetAvatarDto;
import com.hamming.storim.common.dto.protocol.requestresponse.ConnectRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.ConnectResultDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetAvatarResponseDTO;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarSetDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.Location;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.game.ServerEvent;

public class ConnectAction extends Action<ConnectRequestDTO> {
    private GameController controller;


    public ConnectAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection)  getClient();
        UserDto verifiedUser = client.verifyUser(getDto().getUserId(), getDto().getToken());
        if ( verifiedUser != null ) {
            client.setCurrentUser(verifiedUser);
            Long roomId = getDto().getRoomId();
            client.sendGameState();
            client.setRoom(roomId);
            // Send current User info
            UserDto currentUser = client.getCurrentUser();
            Location location = controller.getGameState().getLocation(currentUser.getId());
            LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(location);
            SetCurrentUserDTO setCurrentUserDTO = new SetCurrentUserDTO(currentUser, locationDto);
            client.send(setCurrentUserDTO);
            if ( currentUser.getCurrentAvatarID() != null ) {
                //Send Avatar
                GetAvatarResponseDTO response = client.getServer().getDataServerConnection().getAvatar(currentUser.getCurrentAvatarID());
                AvatarSetDTO avatarSetDTO = new AvatarSetDTO(currentUser.getId(), response.getAvatar());
                client.send(avatarSetDTO);
            }
            // Add this user as online user
            controller.getGameState().getOnlineUsers().add(currentUser);
            // RegisterListener for the current Room
            controller.addRoomListener(location.getRoom().getId(), client);
            // Notify the listeners
            controller.fireServerEvent(getClient(), new ServerEvent(ServerEvent.Type.USERCONNECTED, currentUser));
        } else {
            String errorMessage = "Not a valid user or valid token!";
            getClient().send(new ConnectResultDTO(false, errorMessage, null, null));
        }
    }


}
