package com.hamming.loginserver;

import com.hamming.loginserver.action.GetRoomsForServerAction;
import com.hamming.loginserver.action.GetServersAction;
import com.hamming.loginserver.action.LoginAction;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsForServerRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerRegistrationsRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginRequestDTO;
import com.hamming.storim.server.common.ClientConnection;

import java.net.Socket;

public class UserClientConnection extends ClientConnection<LoginServerWorker> {

    private UserDto currentUser;

    public UserClientConnection(String id, Socket s, LoginServerWorker serverWorker) {
        super(id, s, serverWorker);
    }

    @Override
    public void addActions() {
        getProtocolHandler().addAction(LoginRequestDTO.class, new LoginAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetServerRegistrationsRequestDTO.class, new GetServersAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetRoomsForServerRequestDTO.class, new GetRoomsForServerAction(getServerWorker(), this));
    }

    public UserDto getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {

    }
}
