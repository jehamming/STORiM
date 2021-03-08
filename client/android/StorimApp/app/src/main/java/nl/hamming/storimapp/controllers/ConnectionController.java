package nl.hamming.storimapp.controllers;

import nl.hamming.storimapp.interfaces.ConnectionListener;

import com.hamming.storim.game.Protocol;
import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.net.CommandReceiver;
import com.hamming.storim.net.NetClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionController implements CommandReceiver {

    private NetClient client;
    private ProtocolHandler protocolHandler;
    private UserDto user;
    private List<ConnectionListener> connectionListeners;

    private Map<Protocol.Command, List<CommandReceiver>> commandReceivers;


    public ConnectionController() {
        protocolHandler = new ProtocolHandler();
        connectionListeners = new ArrayList<ConnectionListener>();
        commandReceivers = new HashMap<Protocol.Command, List<CommandReceiver>>();
    }

    public void disconnect() {
        if (client != null ) {
            client.dispose();
            fireDisconnectedEvent();
        }
    }

    public void connect(String serverip, int port) throws Exception {
        if (client != null && !client.isConnected()) {
            client.dispose();
        }
        client = new NetClient(this);
        user = null;
        String result = client.connect(serverip, port);
        if (result != null) throw new Exception("Error:" + result);
        fireConnectedEvent();
    }

    public void send(String s) {
        if (client != null ) {
            client.send(s);
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
    public void receiveCommand(Protocol.Command cmd, String[] data) {
        List<CommandReceiver> listReceivers = commandReceivers.get(cmd);
        boolean handled = false;
        if (listReceivers != null) {
            for (CommandReceiver c : listReceivers) {
                c.receiveCommand(cmd, data);
                handled = true;
            }
        }
        if (!handled) {
            System.out.println(this.getClass().getName() + ":" + "Command " + cmd.toString() + " NOT handled");
        }

    }


    public void registerReceiver(Protocol.Command cmd, CommandReceiver receiver) {
        List<CommandReceiver> listReceivers = commandReceivers.get(cmd);
        if (listReceivers == null) {
            listReceivers = new ArrayList<CommandReceiver>();
        }
        if (!listReceivers.contains(receiver)) {
            listReceivers.add(receiver);
            commandReceivers.put(cmd, listReceivers);
        }
    }

    public void unregisterReceiver(Protocol.Command cmd, CommandReceiver receiver) {
        List<CommandReceiver> listReceivers = commandReceivers.get(cmd);
        if ( listReceivers != null && listReceivers.contains(receiver)) {
            listReceivers.remove(receiver);
        }
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }
}
