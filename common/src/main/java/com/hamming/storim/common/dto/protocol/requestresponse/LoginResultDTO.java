package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class LoginResultDTO extends ResponseDTO {
    private UserDto user;
    private String token;
    private LocationDto location;
    private boolean serverAdmin;
    private boolean userdataServerAdmin;

    public LoginResultDTO(boolean success, String token, String errorMessage, UserDto user, LocationDto location, boolean serverAdmin, boolean userdataServerAdmin) {
        super(success, errorMessage);
        this.user = user;
        this.location = location;
        this.token = token;
        this.serverAdmin = serverAdmin;
        this.userdataServerAdmin = userdataServerAdmin;
    }

    public UserDto getUser() {
        return user;
    }

    public LocationDto getLocation() {
        return location;
    }

    public String getToken() {
        return token;
    }

    public boolean isServerAdmin() {
        return serverAdmin;
    }

    public boolean isUserdataServerAdmin() {
        return userdataServerAdmin;
    }

    @Override
    public String toString() {
        return "LoginResultDTO{" +
                "user=" + user +
                ", success='" + isSuccess() + '\'' +
                ", error='" + getErrorMessage() + '\'' +
                ", token='" + token + '\'' +
                ", location=" + location +
                ", serverAdmin=" + serverAdmin +
                ", userdataServerAdmin=" + userdataServerAdmin +
                '}';
    }
}
