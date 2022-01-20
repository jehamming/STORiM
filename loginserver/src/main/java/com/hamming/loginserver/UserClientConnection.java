package com.hamming.loginserver;

import com.hamming.loginserver.action.GetRoomsForServerAction;
import com.hamming.loginserver.action.GetServersAction;
import com.hamming.loginserver.action.LoginAction;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.ClientTypeDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsForServerRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsForServerResponseDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginRequestDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetServerRegistrationsRequestDTO;
import com.hamming.storim.server.common.ClientConnection;

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
        getProtocolHandler().addAction(LoginRequestDTO.class, new LoginAction(getServerWorker(), this));
        getProtocolHandler().addAction(GetServerRegistrationsRequestDTO.class, new GetServersAction(getServerWorker()));
        getProtocolHandler().addAction(GetRoomsForServerRequestDTO.class, new GetRoomsForServerAction(getServerWorker()));
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
    }
}
