package nl.hamming.storimapp.interfaces;

import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.model.dto.UserLocationDto;

public interface MovementListener {

    public void userMoved(UserDto user, UserLocationDto newUserLocation);

    public void teleported(UserLocationDto location);

}
