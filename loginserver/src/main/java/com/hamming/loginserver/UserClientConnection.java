package com.hamming.loginserver;

import com.hamming.loginserver.action.GetRoomsForServerAction;
import com.hamming.loginserver.action.GetServersAction;
import com.hamming.loginserver.action.LoginAction;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsForServerDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerRegistrationsDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginDTO;
import com.hamming.storim.server.common.ClientConnection;

import java.net.Socket;

public class UserClientConnection extends ClientConnection {

    private UserDto currentUser;

    public UserClientConnection(String id, Socket s, LoginServerWorker serverWorker) {
        super(id, s, serverWorker);
    }

    @Override
    public void addActions() {
        getProtocolHandler().addAction(new LoginAction((LoginServerWorker) getServerWorker(), this));
        getProtocolHandler().addAction(new GetServersAction((LoginServerWorker) getServerWorker(), this));
        getProtocolHandler().addAction(new GetRoomsForServerAction((LoginServerWorker) getServerWorker(), this));
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
