package com.hamming.loginserver;

import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;

import java.net.Socket;

public class UserDataServerConnection extends ClientConnection {


    public UserDataServerConnection(String id, Socket s, ServerWorker serverWorker) {
        super(id, s, serverWorker);
    }

    @Override
    public void addActions() {
        //TODO what kind of messages do you receive from the userdataserver?
    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {

    }
}
