package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.factories.DTOFactory;
import com.hamming.storim.server.model.User;
import com.hamming.storim.server.factories.UserFactory;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.protocol.LoginRequestDTO;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.LoginResultDTO;

public class LoginAction extends Action<LoginRequestDTO> {
    private GameController controller;
    private ClientConnection client;


    public LoginAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        User u = UserFactory.getInstance().validateUser(getDto().getUsername(), getDto().getPassword());
        if ( u != null ) {
            if (controller.getGameState().getOnlineUsers().contains(u)) {
                // Already online
                client.setCurrentUser(null);
                LoginResultDTO dto = new LoginResultDTO(false, "You are already logged in!", null, null);
                client.send(dto);
            } else {
                // Connected!
                client.setCurrentUser(u);
                client.getProtocolHandler().LoggedIn();
                UserDto userDTO = DTOFactory.getInstance().getUserDTO(u);
                LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(u.getLocation());
                LoginResultDTO loginResultDTO = new LoginResultDTO(true, null, userDTO, locationDto);
                client.sendGameState(u);
                client.sendRoom(u.getLocation().getRoom());
                client.send(loginResultDTO);
                client.sendThingsInRoom(u.getLocation().getRoom());
            }
        } else {
            // Invalid user/password
            client.setCurrentUser(null);
            LoginResultDTO dto = new LoginResultDTO(false, "Not a valid username/password combo", null, null);
            client.send(dto);
        }
    }

}
