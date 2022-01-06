package com.hamming.storim.common.interfaces;

import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;

public interface UserListener {
     void userConnected(UserDto user);
     void userUpdated(UserDto user);
     void userDisconnected(UserDto user);
     void userOnline(UserDto user);
     void userTeleported(Long userId, LocationDto location);
     void avatarAdded(AvatarDto avatar);
     void avatarDeleted(AvatarDto avatar);
     void avatarUpdated(AvatarDto avatar);
     void loginResult(boolean success, String message);
}
