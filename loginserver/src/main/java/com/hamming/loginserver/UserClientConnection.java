package com.hamming.loginserver;

import com.hamming.loginserver.action.GetServersAction;
import com.hamming.loginserver.action.LoginAction;
import com.hamming.loginserver.action.VerifyUserAction;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ClientTypeDTO;
import com.hamming.storim.common.dto.protocol.login.LoginRequestDTO;
import com.hamming.storim.common.dto.protocol.login.GetServersRequestDTO;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.loginserver.VerifyUserRequestDTO;
import com.hamming.storim.server.common.model.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserClientConnection extends ClientConnection<LoginServerWorker> {

    private UserDto currentUser;

    public UserClientConnection(ClientTypeDTO clientTypeDTO, Socket s, ObjectInputStream in, ObjectOutputStream out, LoginServerWorker serverWorker) {
        super(clientTypeDTO, s, in, out, serverWorker);
    }

    @Override
    public void connectionClosed() {
        //TODO Implement?
    }

    @Override
    public void addActions() {
        // TODO Meer Actions
        getProtocolHandler().addAction(LoginRequestDTO.class, new LoginAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetServersRequestDTO.class, new GetServersAction(getServerWorker()));
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
    }
}
