package com.hamming.storim.controllers;

import com.hamming.storim.interfaces.ConnectionListener;
import com.hamming.storim.interfaces.UserListener;
import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.LocationDto;
import com.hamming.storim.model.dto.protocol.*;
import com.hamming.storim.net.NetCommandReceiver;

import java.util.*;

public class UserController implements ConnectionListener {

    private ProtocolHandler protocolHandler;
    private ConnectionController connectionController;
    private List<UserListener> userListeners;
    private List<UserDto> users;
    private Map<Long, LocationDto> userLocations;
    private UserDto currentUser;

    public UserController(ConnectionController connectionController) {
        this.connectionController = connectionController;
        connectionController.addConnectionListener(this);
        protocolHandler = new ProtocolHandler();
        userListeners = new ArrayList<UserListener>();
        users = new ArrayList<UserDto>();
        userLocations = new HashMap<Long, LocationDto>();
        connectionController.registerReceiver(LoginResultDTO.class, new NetCommandReceiver<LoginResultDTO>() {
            @Override
            public void receiveDTO(LoginResultDTO dto) {
                checkLogin(dto);
            }
        });
        connectionController.registerReceiver(UserConnectedDTO.class,new NetCommandReceiver<UserConnectedDTO>() {
            @Override
            public void receiveDTO(UserConnectedDTO dto) {
                userConnected(dto);
            }
        });
        connectionController.registerReceiver(UserDisconnectedDTO.class,new NetCommandReceiver<UserDisconnectedDTO>() {
            @Override
            public void receiveDTO(UserDisconnectedDTO dto) {
                userDisconnected(dto);
            }
        });
        connectionController.registerReceiver(GetUserResultDTO.class,new NetCommandReceiver<GetUserResultDTO>() {
            @Override
            public void receiveDTO(GetUserResultDTO dto) {
                handleGetUserResult(dto);
            }
        });
        connectionController.registerReceiver(UserOnlineDTO.class,new NetCommandReceiver<UserOnlineDTO>() {
            @Override
            public void receiveDTO(UserOnlineDTO dto) {
                handleUserOnlineDTO(dto);
            }
        });
        connectionController.registerReceiver(UserTeleportedDTO.class,new NetCommandReceiver<UserTeleportedDTO>() {
            @Override
            public void receiveDTO(UserTeleportedDTO dto) {
                handleUserTeleportedDTO(dto);
            }
        });
    }

    private void handleUserTeleportedDTO(UserTeleportedDTO dto) {
        userLocations.put(dto.getUserId(), dto.getLocation());
        for (UserListener l: userListeners) {
            l.userTeleported(dto.getUserId(), dto.getLocation());
        }
    }

    private void handleUserOnlineDTO(UserOnlineDTO dto) {
        users.add(dto.getUser());
        userLocations.put(dto.getUser().getId(), dto.getLocation());
        for (UserListener l: userListeners) {
            l.userOnline(dto.getUser());
        }
    }


    public void sendLogin(String username, String password) {
        LoginRequestDTO dto = protocolHandler.getLoginDTO(username, password);
        connectionController.send(dto);
    }

    private void checkLogin( LoginResultDTO dto) {
        if (dto.isLoginSucceeded()) {
            currentUser = dto.getUser();
            userLocations.put(dto.getUser().getId(), dto.getLocation());
            users.add(currentUser);
        } else {
            currentUser = null;
        }
        sendLoginResult(dto.isLoginSucceeded(), dto.getErrorMessage());
    }


    public void sendLoginResult(boolean success, String msg) {
        for (UserListener userListener: userListeners) {
            userListener.loginResult(success, msg);
        }
    }

    public void addUserListener(UserListener l) {
        userListeners.add(l);
    }

    public UserDto getCurrentUser() {
        return currentUser;
    }

    public LocationDto getCurrentUserLocation() {
        return userLocations.get(currentUser.getId());
    }

    private void handleGetUserResult(GetUserResultDTO dto) {
        if (dto.isSuccess()) {
            users.add(dto.getUser());
        } else {
            System.out.println("Get user failed: " + dto.getErrorMessage());
        }
    }

    private void userConnected(UserConnectedDTO dto) {
        users.add(dto.getUser());
        userLocations.put(dto.getUser().getId(), dto.getLocation());
        for (UserListener l: userListeners) {
            l.userConnected(dto.getUser());
        }
    }

    private void userDisconnected(UserDisconnectedDTO dto) {
        UserDto user = findUserById(dto.getUserID());
        if ( user != null ) {
            users.remove(user);
            userLocations.remove(user.getId());
            for (UserListener l: userListeners) {
                l.userDisconnected(user);
            }
        }
    }

    public UserDto findUserByID(Long userId) {
        UserDto user = findUserById(userId);
        return user;
    }

    public void setUserLocation(Long userId, LocationDto loc) {
        userLocations.put(userId, loc);
    }

    public void setCurrentUserLocation(LocationDto loc) {
        userLocations.put(currentUser.getId(), loc);
    }

    public LocationDto getUserLocation(Long userId) {
        return userLocations.get(userId);
    }

    private UserDto findUserById(Long userId) {
        UserDto found = null;
        for (UserDto user : users) {
            if (user.getId().equals(userId)) {
                found = user;
                break;
            }
        }
        return found;
    }


    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {

    }
}
