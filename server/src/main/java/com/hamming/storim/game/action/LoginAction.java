package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.DTOFactory;
import com.hamming.storim.model.User;
import com.hamming.storim.factories.UserFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.dto.DTO;
import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.RoomDto;
import com.hamming.storim.model.dto.protocol.GetRoomResultDTO;
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
                RoomDto roomDto = DTOFactory.getInstance().getRoomDto(u.getLocation().getRoom());
                GetRoomResultDTO getRoomResultDTO = DTOFactory.getInstance().getRoomResultDTO(true, null, roomDto);
                client.send(getRoomResultDTO);
                client.send(loginResultDTO);
                client.sendFullGameState();
            }
        } else {
            // Invalid user/password
            client.setCurrentUser(null);
            LoginResultDTO dto = new LoginResultDTO(false, "Not a valid username/password combo", null, null);
            client.send(dto);
        }
    }

}
