package com.hamming.storim.common.controllers;

import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.NetClient;
import com.hamming.storim.common.net.ProtocolReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionController implements ProtocolReceiver, ConnectionListener {

    private NetClient client;
    private String clientID;
    private UserDto user;
    private List<ConnectionListener> connectionListeners;

    private Map<Class, List<ProtocolReceiver>> commandReceivers;


    public ConnectionController(String clientID) {
        this.clientID = clientID;
        connectionListeners = new ArrayList<ConnectionListener>();
        commandReceivers = new HashMap<Class, List<ProtocolReceiver>>();
    }

    public void disconnect() {
        if (client != null ) {
            client.dispose();
            fireDisconnectedEvent();
        }
    }

    public void connect(String clientName, String serverip, int port) throws Exception {
        if (client != null && client.isConnected()) {
            client.dispose();
        }
        client = new NetClient(this, this , serverip, port);
        user = null;
        while (!client.isConnected()) {
            try {
                Thread.sleep(100);
                //TODO timeout ???
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        fireConnectedEvent();
    }

    public void fireConnectedEvent() {
        for (ConnectionListener l: connectionListeners) {
            l.connected();
        }
    }

    public void fireDisconnectedEvent() {
        for (ConnectionListener l: connectionListeners) {
            l.disconnected();
        }
    }


    public void addConnectionListener(ConnectionListener listener) {
        if ( !connectionListeners.contains(listener) ) {
            connectionListeners.add(listener);
        }
    }

    public void removeConnectionListener(ConnectionListener listener) {
        if ( connectionListeners.contains(listener) ) {
            connectionListeners.remove(listener);
        }
    }



    @Override
    public void receiveDTO(ProtocolDTO dto) {
        List<ProtocolReceiver> listReceivers = commandReceivers.get(dto.getClass());
        if (listReceivers != null) {
            for (ProtocolReceiver c : listReceivers) {
                c.receiveDTO(dto);
            }
        }
    }


    public void registerReceiver(Class c, ProtocolReceiver receiver) {
        List<ProtocolReceiver> listReceivers = commandReceivers.get(c);
        if (listReceivers == null) {
            listReceivers = new ArrayList<ProtocolReceiver>();
        }
        if (!listReceivers.contains(receiver)) {
            listReceivers.add(receiver);
            commandReceivers.put(c, listReceivers);
        }
    }

    public void unregisterReceiver(Class c, ProtocolReceiver receiver) {
        List<ProtocolReceiver> listReceivers = commandReceivers.get(c);
        if ( listReceivers != null && listReceivers.contains(receiver)) {
            listReceivers.remove(receiver);
        }
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    public void send(ProtocolDTO protocolDTO) {
        client.send(protocolDTO);
    }

    public <T extends ResponseDTO> T sendReceive(ProtocolDTO requestDTO, Class<T> responseClass) {
        return responseClass.cast( client.sendReceive(requestDTO, responseClass));
    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {

    }
}
