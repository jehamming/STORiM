package com.hamming.storim.server;

import com.hamming.storim.common.dto.protocol.requestresponse.GetRoomsDTO;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.game.action.GetRoomsAction;

import java.net.Socket;

public class LoginServerConnection extends ClientConnection{


    public LoginServerConnection(String id, Socket s, ServerWorker serverWorker) {
        super(id, s, serverWorker);
    }


    @Override
    public void addActions() {
        //What kind of messages do you receive from the LoginServer?
        getProtocolHandler().addAction(GetRoomsDTO.class, new GetRoomsAction(this));
    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {

    }
}
