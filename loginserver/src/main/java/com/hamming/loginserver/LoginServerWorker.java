package com.hamming.loginserver;

import com.hamming.storim.server.ServerWorker;

import java.util.ArrayList;
import java.util.List;

public class LoginServerWorker extends ServerWorker {

    private List<ServerRegistration> registeredServers;
    private STORIMLoginServer loginServer;

    public LoginServerWorker(STORIMLoginServer loginServer){
        registeredServers = new ArrayList<ServerRegistration>();
        this.loginServer = loginServer;
    }

    public String addServer(String name, String url, int port) {
        String errorMessage = null;
        if ( findServerRegistration(name) == null ) {
            ServerRegistration registration = new ServerRegistration(name, url, port);
            registeredServers.add(registration);
            System.out.println("New Server registered: " + registration);
            System.out.println("No of servers registered: " + registeredServers.size());
        } else {
            errorMessage = name + " already registered as server!";
        }
        return errorMessage;
    }

    public List<ServerRegistration> getRegisteredServers() {
        return registeredServers;
    }
    public ServerRegistration removeRegisteredServer(String servername) {
        ServerRegistration found = findServerRegistration(servername);
        if ( found != null ) {
            registeredServers.remove(found);
            System.out.println("Server "+ servername+ " removed, no of servers registered: " + registeredServers.size());
        }
        return found;
    }

    public ServerRegistration findServerRegistration(String servername) {
        ServerRegistration found = null;
        for (ServerRegistration r : registeredServers  ) {
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
