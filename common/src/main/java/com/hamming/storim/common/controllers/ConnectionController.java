package com.hamming.storim.common.controllers;

import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ClientIdentificationDTO;
import com.hamming.storim.common.dto.protocol.Protocol;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.NetClient;
import com.hamming.storim.common.net.ProtocolReceiver;
import com.hamming.storim.common.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionController implements ProtocolReceiver, ConnectionListener {

    private NetClient client;
    private String clientID;
    private UserDto user;
    private List<ConnectionListener> connectionListeners;
    private static int CONNECTION_TIMEOUT = 4000;

    private Map<Class, List<ProtocolReceiver>> commandReceivers;

    public ConnectionController(String clientID) {
        this.clientID = clientID;
        connectionListeners = new ArrayList<ConnectionListener>();
        commandReceivers = new HashMap<Class, List<ProtocolReceiver>>();
    }

    public void disconnect() {
        if (client != null ) {
            client.dispose();
        }
    }

    public void connect(String clientName, String serverip, int port) throws Exception {
        int millisecs = 0;
        boolean timeout = false;
        if (client != null && client.isConnected()) {
            client.dispose();
        }
        client = new NetClient(this, this);
        client.setId(clientName);
        String errorMessage = client.connect(serverip, port);
        if ( errorMessage == null ) {
            user = null;
            while (!client.isConnected() && !timeout) {
                try {
                    Thread.sleep(100);
                    Logger.info("Trying to connect to " + serverip);
                    millisecs += 100;
                    if (millisecs > CONNECTION_TIMEOUT) {
                        errorMessage = "Timeout waiting for server (slow network?)";
                        timeout = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            fireConnectedEvent();
        }

        if ( errorMessage != null ) {
            throw new Exception (errorMessage);
        }
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
            Protocol.getInstance().registerClass(c.getSimpleName(), c);
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
        // Disconnected from server
        fireDisconnectedEvent();
    }

}
