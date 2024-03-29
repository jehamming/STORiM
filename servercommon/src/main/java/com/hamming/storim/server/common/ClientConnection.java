package com.hamming.storim.server.common;


import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;
import com.hamming.storim.common.interfaces.Client;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.NetClient;
import com.hamming.storim.common.net.ProtocolReceiver;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.BasicObject;

import java.net.Socket;

//  ClientConnection, able to handle Async traffic and Sync actions
public abstract class ClientConnection implements Client, ProtocolReceiver, ConnectionListener {

    private UserDto currentUser;
    private ProtocolHandler<Action> protocolHandler;
    private String id;
    private ServerWorker serverWorker;
    private NetClient netClient;

    private boolean serverAdmin;

    private String sessionToken;

    public ClientConnection(String id, Socket s, ServerWorker serverWorker) {
        this.id = id;
        serverAdmin = false;
        netClient = new NetClient(this, this,this);
        protocolHandler = new ProtocolHandler();
        this.serverWorker = serverWorker;
        addActions();
        netClient.connect(s);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public void receiveDTO(ProtocolDTO dto) {
        Action action = protocolHandler.getAction(dto);
        Logger.info(this, getId(),"Received:" + dto);
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

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public boolean isServerAdmin() {
        return serverAdmin;
    }

    public void setServerAdmin(boolean serverAdmin) {
        this.serverAdmin = serverAdmin;
    }

    public UserDto getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isAuthorized(BasicObject b) {
        boolean authorized = false;
        if ( currentUser != null ) {
            authorized = b.getOwnerId().equals(currentUser.getId()) || b.getEditors().contains( currentUser.getId() ) || isServerAdmin();
        }
        return authorized;
    }


}
