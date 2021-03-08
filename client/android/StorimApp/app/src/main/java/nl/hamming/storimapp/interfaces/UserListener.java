package nl.hamming.storimapp.interfaces;

import com.hamming.storim.model.dto.UserDto;

public interface UserListener {

    public void userConnected(UserDto user);
    public void userDisconnected(UserDto user);

    public void loginResult(boolean success, String message);

}
