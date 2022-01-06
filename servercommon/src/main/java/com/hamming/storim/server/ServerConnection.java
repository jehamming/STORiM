package com.hamming.storim.server;

import com.hamming.storim.common.dto.protocol.ClientTypeDTO;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection implements Runnable {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean running = false;
    private String servername;
    private String name;
    private int serverport;

    public ServerConnection(String name, String servername, int serverport) {
        this.servername = servername;
        this.name = name;
        this.serverport = serverport;
    }

    @Override
    public void run() {
        Socket socket;
        try {
            socket = new Socket(servername, serverport);
            in = new ObjectInputStream( socket.getInputStream() );
            out = new ObjectOutputStream( socket.getOutputStream() );
            out.writeObject(new ClientTypeDTO(name, ClientTypeDTO.TYPE_SERVER));
            running = true;
            while (running) {
                try {
                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                socket.close();
            } catch (IOException e) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(this.getClass().getName() + ":" + "Client Socket closed");
    }

    public ProtocolDTO serverRequest(ProtocolDTO dto) {
        ProtocolDTO result = null;
        try {
            out.writeObject(dto);
            if ( dto.getType() == ProtocolDTO.SYNC ) {
                result = (ProtocolDTO) in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isRunning() {
        return running;
    }
}
