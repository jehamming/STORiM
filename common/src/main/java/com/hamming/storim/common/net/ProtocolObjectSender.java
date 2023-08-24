package com.hamming.storim.common.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.hamming.storim.common.dto.protocol.Protocol;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.ResponseDTO;
import com.hamming.storim.common.util.Logger;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayDeque;
import java.util.Queue;

public class ProtocolObjectSender implements Runnable {


    private ObjectOutputStream out;
    private boolean running = false;
    private Queue<ProtocolDTO> itemsToSend;
    private String id;
    private static int INTERVAL = 50; // Milliseconds, 20Hz
    private Gson gson;

    public ProtocolObjectSender(String id, ObjectOutputStream out) {
        this.out = out;
        this.id = id;

        // Java to JSON
        gson = new Gson();

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
                    //Logger.info(this,id, "Send:" + dto );

                    //To JSON!
                    String json = toJson(dto);

                    Logger.info(this, id, "Send DTO as JSON:" + dto);
                    out.writeObject(json);
                    out.flush();
                } catch (InvalidClassException e) {
                    Logger.error(e.getClass().getSimpleName() + "-" + e.getMessage());
                } catch (NotSerializableException e) {
                    Logger.error(e.getClass().getSimpleName() + "-" + e.getMessage());
                } catch (IOException e) {
                    Logger.error(e.getClass().getSimpleName() + "-" + e.getMessage());
                    running = false;
                }
            }
            long stop = System.currentTimeMillis();
            long timeSpent = start - stop;
            if (timeSpent < INTERVAL) {
                try {
                    Thread.sleep(INTERVAL - timeSpent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String toJson(ProtocolDTO dto) {
        JsonElement element = gson.toJsonTree(dto);
        if (element.isJsonObject()) {
            element.getAsJsonObject().addProperty(ProtocolObjectSerializer.CLASS_PROPERTY_NAME, dto.getClass().getSimpleName());
        }
        String json = gson.toJson(element);
        return json;
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
            Logger.info(this, " ERROR, SYNC Message (" + dto + ") did not have a result! (" + reason + ") ");
        }
        return responseContainer.getResponse();
    }
}
