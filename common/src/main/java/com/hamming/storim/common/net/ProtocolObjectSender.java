package com.hamming.storim.common.net;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Queue;

public class ProtocolObjectSender implements Runnable {


    private ObjectOutputStream out;
    private boolean running = false;
    private Queue<ProtocolDTO> itemsToSend;
    private String id;
    private static int INTERVAL = 50; // Milliseconds, 20Hz

    public ProtocolObjectSender(String id, ObjectOutputStream out) {
        this.out = out;
        this.id = id;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        itemsToSend = new ArrayDeque<>();
        running = true;
        while (running) {
            long start = System.currentTimeMillis();
            while (!itemsToSend.isEmpty()) {
                ProtocolDTO dto = itemsToSend.remove();
                try {
                    System.out.println("(" + getClass().getSimpleName() + "-"+id+") Send:" + dto );
                    out.writeObject(dto);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            long stop = System.currentTimeMillis();
            long timeSpent = start - stop;
            if (timeSpent < INTERVAL ) {
                try {
                    Thread.sleep(INTERVAL - timeSpent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopSending() {
        try {
            out.close();
            running = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return running;
    }

    private void enQueue(ProtocolDTO dto) {

        synchronized (itemsToSend) {
            itemsToSend.add(dto);
        }
    }

    public void send(ProtocolDTO dto) {
        enQueue(dto);
    }

    public ResponseDTO sendReceive(ProtocolDTO dto, ResponseContainer responseContainer) {
        enQueue(dto);
        String reason = "TIMEOUT";
        synchronized (responseContainer) {
            try {
                responseContainer.wait(ResponseContainer.RESPONSE_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
                reason = e.toString();
            }
        }
        if (responseContainer.getResponse() == null) {
            System.out.println("(" + getClass().getSimpleName() + ") ERROR, SYNC Message (" + dto + ") did not have a result! ("+reason+") ");
        }
        return responseContainer.getResponse();
    }
}
