package com.hamming.storim.server.game;

import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.serverpush.MessageInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserLeftRoomDTO;
import com.hamming.storim.common.util.StringUtils;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.DTOFactory;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController extends ServerWorker {
    private GameState gameState;

    private Map<Long, List<RoomListener>> roomListeners;
    private List<ServerListener> serverListeners;

    public GameController() {
        gameState = new GameState();
        roomListeners = new HashMap<>();
        serverListeners = new ArrayList<>();
    }

    public void addRoomListener(Long roomId, RoomListener l) {
        List<RoomListener> listenersForRoom = roomListeners.get(roomId);
        if (listenersForRoom == null) {
            listenersForRoom = new ArrayList<>();
            roomListeners.put(roomId, listenersForRoom);
        }
        listenersForRoom.add(l);
    }

    public void removeRoomListener(Long roomId, RoomListener l) {
        List<RoomListener> listenersForRoom = roomListeners.get(roomId);
        if (listenersForRoom != null) {
            listenersForRoom.remove(l);
        }
    }

    public void addServerListener(ServerListener l) {
        serverListeners.add(l);
    }

    public void removeServerListener(ServerListener l) {
        serverListeners.remove(l);
    }

    public void fireRoomEvent(ClientConnection source, Long roomId, RoomEvent event) {
        List<RoomListener> listenersForRoom = roomListeners.get(roomId);
        if (listenersForRoom != null) {
            for (RoomListener listener : listenersForRoom) {
                if (!listener.equals(source)) { // No event to the source of this call
                    listener.roomEvent(event);
                }
            }
        }
    }

    public void fireServerEvent(ClientConnection source, ServerEvent event) {
        for (ServerListener listener : serverListeners) {
            if (!listener.equals(source)) { // No event to the source of this call
                listener.serverEvent(event);
            }
        }
    }

    public GameState getGameState() {
        return gameState;
    }

}