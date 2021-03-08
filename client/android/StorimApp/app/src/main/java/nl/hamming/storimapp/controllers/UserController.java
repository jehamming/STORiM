package nl.hamming.storimapp.controllers;

import nl.hamming.storimapp.interfaces.ConnectionListener;
import nl.hamming.storimapp.interfaces.UserListener;
import com.hamming.storim.game.Protocol;
import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.UserLocationDto;
import com.hamming.storim.net.CommandReceiver;

import java.util.*;

public class UserController implements ConnectionListener, CommandReceiver {

    private ProtocolHandler protocolHandler;
    private ConnectionController connectionController;
    private List<UserListener> userListeners;
    private List<UserDto> users;
    private Map<String, UserLocationDto> userLocations;
    private UserDto currentUser;

    public UserController(ConnectionController connectionController) {
        this.connectionController = connectionController;
        connectionController.addConnectionListener(this);
        protocolHandler = new ProtocolHandler();
        userListeners = new ArrayList<UserListener>();
        users = new ArrayList<UserDto>();
        userLocations = new HashMap<String, UserLocationDto>();
        connectionController.registerReceiver(Protocol.Command.LOGIN,this);
        connectionController.registerReceiver(Protocol.Command.USERCONNECTED,this);
        connectionController.registerReceiver(Protocol.Command.USERDISCONNECTED,this);
        connectionController.registerReceiver(Protocol.Command.GETUSER,this);
    }

    @Override
    public void connected() {
        connectionController.registerReceiver(Protocol.Command.LOGIN, this);
    }

    @Override
    public void disconnected() {
        connectionController.unregisterReceiver(Protocol.Command.LOGIN,this);
    }

    public void sendLogin(String username, String password) {
        String s = protocolHandler.getLoginCommand(username, password);
        connectionController.send(s);
    }

    private void checkLoginOk( String[] data) {
        String status = data[0];
        String[] values = Arrays.copyOfRange(data, 1, data.length);
        String msg = "";
        boolean success = false;
        if (Protocol.SUCCESS.equals(status)) {
            currentUser = new UserDto();
            currentUser.setValues(values);
            users.add(currentUser);
            success = true;
        } else {
            currentUser = null;
            msg = Arrays.toString(values);
        }
        sendLoginResult(success, msg);
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

    @Override
    public void receiveCommand(Protocol.Command cmd, String[] data) {
        switch (cmd) {
            case LOGIN:
                checkLoginOk(data);
                break;
            case GETUSER:
                handleGetUser(data);
                break;
            case USERCONNECTED:
                userConnected(data);
                break;
            case USERDISCONNECTED:
                userDisconnected(data);
                break;
        }
    }

    private void handleGetUser(String[] data) {
        String status = data[0];
        String[] values = Arrays.copyOfRange(data, 1, data.length);
        String msg = "";
        if (Protocol.SUCCESS.equals(status)) {
            UserDto user = new UserDto();
            user.setValues(values);
            users.add(user);
        } else {
            msg = Arrays.toString(values);
            System.out.println("Get user failed: " + msg);
        }
    }

    private void userConnected(String[] data) {
        UserDto user = new UserDto();
        user.setValues(data);
        users.add(user);
        for (UserListener l: userListeners) {
            l.userConnected(user);
        }
    }

    private void userDisconnected(String[] data) {
        UserDto user = new UserDto();
        user.setValues(data);
        users.remove(user);
        userLocations.remove(user.getId());
        for (UserListener l: userListeners) {
            l.userDisconnected(user);
        }
    }

    public UserDto getUser(String userId) {
        UserDto user = findUserById(userId);
        if (user == null ) {
            connectionController.send(protocolHandler.getUserCommand(userId));
            // TODO Wait for user details?
        }
        return user;
    }

    public void setUserLocation(UserLocationDto loc) {
        userLocations.put(loc.getUserId(), loc);
    }

    public UserLocationDto getUserLocation(String userId) {
        return userLocations.get(userId);
    }

    private UserDto findUserById(String userId) {
        UserDto found = null;
        for (UserDto user : users) {
            if (user.getId().equals(userId)) {
                found = user;
                break;
            }
        }
        return found;
    }


}
