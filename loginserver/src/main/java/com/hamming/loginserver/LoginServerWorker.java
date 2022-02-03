package com.hamming.loginserver;

import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.sun.security.ntlm.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginServerWorker extends ServerWorker {

    private Map<ClientConnection,ServerRegistration> registeredServers;
    private STORIMLoginServer loginServer;

    public LoginServerWorker(STORIMLoginServer loginServer){
        registeredServers = new HashMap<>();
        this.loginServer = loginServer;
    }

    public String addServer(ClientConnection connection, String name, String url, int port) {
        String errorMessage = null;
        if ( findServerRegistration(name) == null ) {
            ServerRegistration registration = new ServerRegistration(connection, name, url, port);
            registeredServers.put(connection, registration);
            System.out.println("("+getClass().getSimpleName() +") New Server registered: " + registration);
            System.out.println("("+getClass().getSimpleName() +") No of servers registered: " + registeredServers.size());
        } else {
            errorMessage = name + " already registered as server!";
        }
        return errorMessage;
    }

    public List<ServerRegistration> getRegisteredServers() {
        List<ServerRegistration> result = new ArrayList<>(registeredServers.values());
        return result;
    }

    public ServerRegistration removeRegisteredServer(ClientConnection connection) {
        ServerRegistration serverRegistration = registeredServers.get(connection);
        if ( serverRegistration != null ) {
            registeredServers.remove(serverRegistration);
            System.out.println("("+getClass().getSimpleName() +") Server "+ serverRegistration.getServerName()+ " removed, no of servers registered: " + registeredServers.values().size());
        }
        return serverRegistration;
    }

    public ServerRegistration findServerRegistration(String servername) {
        ServerRegistration found = null;
        for (ServerRegistration r : registeredServers.values()  ) {
            if (r.getServerName().equals(servername)) {
                found = r;
                break;
            }
        }
        return found;
    }


    public STORIMLoginServer getLoginServer() {
        return loginServer;
    }
}
