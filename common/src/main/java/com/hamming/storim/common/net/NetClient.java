package com.hamming.storim.common.net;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.RequestDTO;
import com.hamming.storim.common.dto.protocol.RequestResponseDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetClient<T extends ResponseDTO> implements Runnable {
    private static final long RESPONSE_TIMEOUT = 2000;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean running = false;
    private NetCommandReceiver receiver;
    private ResponseContainer responseContainer;

    public NetClient(NetCommandReceiver receiver) {
        this.receiver = receiver;
        responseContainer = new ResponseContainer();

    }

    public String connect(String ip, int port) {
        String retval = null;
        try {
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Thread clientThread = new Thread(this);
            clientThread.setName("Client Connection");
            clientThread.setDaemon(true);
            clientThread.start();
            running = true;
        } catch (IOException e) {
            System.out.println(this.getClass().getName() + ":" + "ERROR:" + e.getMessage());
            retval = e.getMessage();
            //e.printStackTrace();
        }
        return retval;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Object read = in.readObject();
                ProtocolDTO dto = (ProtocolDTO) read;
                if ( dto instanceof ResponseDTO) {
                    ResponseDTO response = (ResponseDTO) dto;

                    if (response.getClass().equals(responseContainer.getResponseClass())) {
                        responseContainer.setResponse(response);
                        synchronized (responseContainer) {
                            responseContainer.notify();
                        }
                    } else {
                        System.out.println(this.getClass().getName() + ":Received ResponseDTO I was NOT waiting for! :" + dto.toString());
                    }
                }
                System.out.println(this.getClass().getName() + ":RECEIVED:" + dto.toString());
                received(dto);
            } catch (IOException e) {
               // System.out.println(this.getClass().getName() + ":" + "Error:" + e.getMessage());
               // e.printStackTrace();
                running = false;
            } catch (ClassNotFoundException e) {
                System.out.println(this.getClass().getName() + ":" + "Error:" + e.getMessage());
                e.printStackTrace();
            }
        }

        if ( socket != null ) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        System.out.println(this.getClass().getName() + ":" + "NetClient finished");
    }

    public void send(RequestDTO pDTO) {
        try {
            System.out.println(this.getClass().getName() + ":" + "Send:" + pDTO );
            out.writeObject(pDTO);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResponseDTO sendReceive(RequestResponseDTO requestResponseDTO, Class responseClass) {
        try {
            responseContainer.setResponse(null);
            responseContainer.setResponseClass(responseClass);
            System.out.println(this.getClass().getName() + ":" + "SendReceive:" + requestResponseDTO + ", waiting for: " + responseClass.getSimpleName());
            out.writeObject(requestResponseDTO);
            synchronized (responseContainer) {
                try {
                    responseContainer.wait(RESPONSE_TIMEOUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (responseContainer.getResponse() == null ) {
            System.out.println("("+getClass().getSimpleName() +") ERROR, SYNChronous Message ("+requestResponseDTO.getClass().getSimpleName()+") did not have a result!  " );
        }
        return responseContainer.getResponse();
    }

    public void received(ProtocolDTO dto) {
        receiver.receiveDTO(dto);
    }


    public void dispose() {
        try {
            if (running) {
                running = false;
                closeConnection();
            }
            socket = null;
            in = null;
            out = null;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void closeConnection() throws IOException {
        try {
            out.close();
        } finally {
            try {
                in.close();
            } finally {
                socket.close();
            }
        }

    }


    public boolean isConnected() {
        return running;
    }

}
