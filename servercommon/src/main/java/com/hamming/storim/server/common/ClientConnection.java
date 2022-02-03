package com.hamming.storim.server.common;


import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.NetClient;
import com.hamming.storim.common.net.ProtocolReceiver;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.action.Action;

import java.net.Socket;

//  ClientConnection, able to handle Async traffic and Sync actions
public abstract class ClientConnection implements ProtocolReceiver, ConnectionListener {

    private ProtocolHandler<Action> protocolHandler;
    private String id;
    private ServerWorker serverWorker;
    private NetClient netClient;

    public ClientConnection(String id, Socket s, ServerWorker serverWorker) {
        this.id = id;
        netClient = new NetClient(this,this, s);
        protocolHandler = new ProtocolHandler();
        this.serverWorker = serverWorker;
        addActions();
    }

    @Override
    public void receiveDTO(ProtocolDTO dto) {
        Action action = protocolHandler.getAction(dto);
        System.out.println("(" + getClass().getSimpleName() + ") Received:" + dto);
        if (action != null) {
            action.setDTO(dto);
            serverWorker.addAction(action);
        }
    }

    public abstract void addActions();

    public boolean isConnected() {
        return netClient != null && netClient.isConnected();
    }

    public void send(ProtocolDTO protocolDTO) {
        netClient.send(protocolDTO);
    }

    public <T extends ResponseDTO> T sendReceive(ProtocolDTO requestDTO, Class<T> responseClass) {
        return responseClass.cast( netClient.sendReceive(requestDTO, responseClass));
    }

    public ProtocolHandler getProtocolHandler() {
        return protocolHandler;
    }

    public ServerWorker getServerWorker() {
        return serverWorker;
    }

    public String getId() {
        return id;
    }
}
