package com.hamming.storim;


import com.hamming.storim.factories.DTOFactory;
import com.hamming.storim.game.*;
import com.hamming.storim.game.action.*;
import com.hamming.storim.model.Verb;
import com.hamming.storim.model.VerbOutput;
import com.hamming.storim.model.Room;
import com.hamming.storim.model.User;
import com.hamming.storim.model.dto.*;
import com.hamming.storim.model.dto.protocol.*;

import java.io.*;
import java.net.Socket;

public class ClientConnection implements Runnable, GameStateListener {

    private User currentUser;
    private Socket socket;
    private ObjectInputStream in;
    private boolean running = true;
    private GameController gameController;
    private GameProtocolHandler gameProtocolHandler;
    private ClientSender clientSender;
    private String id;

    public ClientConnection(String id, Socket s, ObjectInputStream in, ObjectOutputStream out, GameController controller) {
        this.socket = s;
        this.in = in;
        this.id = id;
        this.gameController = controller;
        this.gameProtocolHandler = new GameProtocolHandler(controller, this);
        clientSender = new ClientSender(out);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Object read = in.readObject();
                DTO dto = (DTO) read;
                System.out.println("RECEIVED:" + dto);
                handleInput(dto);
            } catch (IOException e) {
                //System.out.println(this.getClass().getName() + ":" + "IO Error:" + e.getMessage());
               // e.printStackTrace();
                running = false;
            } catch (ClassNotFoundException e) {
                System.out.println(this.getClass().getName() + ":" + "Error:" + e.getMessage());
                e.printStackTrace();
            }
        }
        gameController.removeListener(this);
        clientSender.stopSending();
        if (currentUser != null) {
           //gameController.userDisconnected(currentUser);
            UserDisconnectedAction action = new UserDisconnectedAction(gameController, this, currentUser);
            gameController.addAction(action);
        }
        try {
            socket.close();
        } catch (IOException e) {
        }
        System.out.println(this.getClass().getName() + ":" + "Client Socket closed");
    }

    private void handleInput(DTO dto) {
        Action action = gameProtocolHandler.getAction(dto);
        if (action != null ) {
            action.setDTO(dto);
            if (action != null) {
                gameController.addAction(action);
            }
        } else {
            System.out.println("NOT HANDLED:" + dto.getClass().getSimpleName() );
        }
    }

    public void send(DTO dto) {
        clientSender.enQueue(dto);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        if (currentUser != null) {
            gameController.userConnected(currentUser);
        }
    }

    public void sendFullGameState() {
        if (isLoggedIn()) {
            // Send Commnds
            sendUserCommands();
            // Rooms
            sendRooms();
            // TODO Send Inventory
            // Logged in Users;
            for (User u : gameController.getGameState().getOnlineUsers()) {
                if (!u.getId().equals(currentUser.getId())) {
                    sendUserDetails(u);
                    handleUserOnline(u);
                }
            }
           // sendUsersInRoom(currentUser.getLocation().getRoom());
        }
    }

    private void sendRooms() {
        for (Room room : currentUser.getRooms() ) {
            RoomDto roomDto = DTOFactory.getInstance().getRoomDto(room);
            GetRoomResultDTO getRoomResultDTO = DTOFactory.getInstance().getRoomResultDTO(true, null, roomDto);
            send(getRoomResultDTO);
        }
    }

    private void sendUserCommands() {
        for (Verb verb : currentUser.getVerbs() ) {
            VerbDto verbDto = DTOFactory.getInstance().getVerbDto(verb);
            GetVerbResultDTO getCommandResultDTO = DTOFactory.getInstance().getVerbResultDto(true, null, verbDto);
            send(getCommandResultDTO);
        }
    }

    private boolean isOnline(User user) {
        return gameController.getGameState().getOnlineUsers().contains(user);
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    @Override
    public void newGameState(GameStateEvent event) {
        switch (event.getType()) {
            case USERCONNECTED:
                handleUserConnected((User) event.getObject());
                break;
            case USERDISCONNECTED:
                handleUserDisconnected((User) event.getObject());
                break;
            case USERLOCATION:
                handleUserLocation((User) event.getObject());
                break;
            case USERTELEPORTED:
                handleTeleported((User) event.getObject(), (Long) event.getExtraData());
                break;
            case VERBEXECUTED:
                handleVerbExecuted((VerbOutput) event.getObject());
                break;
            case VERBDELETED:
                handleVerbDeleted((Verb) event.getObject());
                break;
            case ROOMDELETED:
                handleRoomDeleted((Room) event.getObject());
                break;

        }
    }

    private void handleRoomDeleted(Room room) {
        currentUser.removeRoom(room);
        send(DTOFactory.getInstance().getRoomDeletedDTO(room));
    }

    private void handleVerbDeleted(Verb verb) {
        currentUser.removeVerb(verb);
        send(DTOFactory.getInstance().getVerbDeletedDTO(verb));
    }
    private void handleVerbExecuted(VerbOutput cmdResult) {
        if ( isInCurrentRoom(cmdResult.getCaller())) {
            String output = "";
            if (cmdResult.getCaller().getId().equals(currentUser.getId())) {
                output = cmdResult.getToCaller();
            } else {
                output = cmdResult.getToLocation();
            }
            ExecVerbResultDTO execVerbResultDTO = DTOFactory.getInstance().getExecVerbResultDto(cmdResult.getId(), output);
            send(execVerbResultDTO);
        }
    }

    private boolean isInCurrentRoom(User caller) {
        return currentUser.getLocation().getRoom().getId().equals(caller.getLocation().getRoom().getId());
    }


    private void handleTeleported(User user, Long oldRoomId) {
        if (user.equals(currentUser)) {
            sendUsersInRoom(user.getLocation().getRoom());
        } else {
            RoomDto roomDto = DTOFactory.getInstance().getRoomDto(user.getLocation().getRoom());
            GetRoomResultDTO getRoomResultDTO = DTOFactory.getInstance().getRoomResultDTO(true, null, roomDto);
            send(getRoomResultDTO);
            LocationDto location = DTOFactory.getInstance().getLocationDTO(user.getLocation());
            UserTeleportedDTO userTeleportedDTO = DTOFactory.getInstance().getUserTeleportedDTO(user, oldRoomId, location);
            send(userTeleportedDTO);
        }
    }

    public void sendUsersInRoom(Room room) {
        gameController.getGameState().getOnlineUsers().forEach(user -> {
            if (!user.equals(currentUser) && room.getId().equals(user.getLocation().getRoom().getId())) {
                LocationDto locationDto = DTOFactory.getInstance().getLocationDTO(user.getLocation());
                UserInRoomDTO dto = DTOFactory.getInstance().getUserInRoomDTO(user, room, locationDto);
                send(dto);
            }
        });
    }

    public void handleUserLocation(User user) {
        UserLocationUpdateDTO userLocationUpdateDTO = DTOFactory.getInstance().getUserLocationUpdateDTO(user);
        send(userLocationUpdateDTO);
    }

    public void sendRoom(Room room) {
        RoomDto roomDto = DTOFactory.getInstance().getRoomDto(room);
        GetRoomResultDTO getRoomResultDTO = DTOFactory.getInstance().getRoomResultDTO(true, null, roomDto);
        send(getRoomResultDTO);
    }

    private void sendUserDetails(User user) {
        UserDto userDto = DTOFactory.getInstance().getUserDTO(user);
        sendRoom(user.getLocation().getRoom());
        GetUserResultDTO getUserResultDTO = DTOFactory.getInstance().getGetUserResultDTO(true, null, userDto);
        send(getUserResultDTO);
    }

    private void handleUserConnected(User u) {
        if (currentUser != null && !currentUser.equals(u)) {
            UserConnectedAction action = new UserConnectedAction(gameController, this, u);
            gameController.addAction(action);
        }
    }

    private void handleUserOnline(User u) {
        if (currentUser != null && !currentUser.equals(u)) {
            UserOnlineAction action = new UserOnlineAction(gameController, this, u);
            gameController.addAction(action);
        }
    }

    private void handleUserDisconnected(User u) {
        if (currentUser != null && !currentUser.equals(u)) {
            UserDisconnectedAction action = new UserDisconnectedAction(gameController, this, u);
            gameController.addAction(action);
        }
    }

    public GameProtocolHandler getProtocolHandler() {
        return gameProtocolHandler;
    }
}
