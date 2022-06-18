package com.hamming.storim.server;

import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.common.action.Action;

import java.util.ArrayDeque;
import java.util.Deque;

public class ServerWorker implements Runnable {
    private Deque<Action> actionQueue;
    private boolean running = true;


    @Override
    public void run() {
        actionQueue = new ArrayDeque<>();
        while (running) {
            if (actionQueue.isEmpty()) {
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    Logger.info(this, ":" + "Exception : method wait was interrupted!");
                }
            }
            while (!actionQueue.isEmpty()) {
                Action cmd = actionQueue.removeFirst();
                cmd.execute();
            }
        }
    }

    public void addAction(Action cmd) {
        actionQueue.addLast(cmd);
        synchronized (this) {
            this.notify();
        }
    }

}