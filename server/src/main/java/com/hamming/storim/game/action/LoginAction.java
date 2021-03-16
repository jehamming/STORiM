package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.DTOFactory;
import com.hamming.storim.model.User;
import com.hamming.storim.factories.UserFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.protocol.LoginRequestDTO;
import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.protocol.LoginResultDTO;

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
                client.sendRoom(u.getLocation().getRoom());
                client.sendFullGameState();
                client.send(loginResultDTO);
            }
        } else {
            // Invalid user/password
            client.setCurrentUser(null);
            LoginResultDTO dto = new LoginResultDTO(false, "Not a valid username/password combo", null, null);
            client.send(dto);
        }
    }

}
