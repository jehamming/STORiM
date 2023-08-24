package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ResponseDTO;

public class LoginWithTokenResultDTO extends ResponseDTO {

    private boolean serverAdmin;
    private boolean userdataServerAdmin;
    public LoginWithTokenResultDTO(boolean success, String errorMessage, boolean serverAdmin, boolean userdataServerAdmin) {
        super(success, errorMessage);
        this.serverAdmin = serverAdmin;
        this.userdataServerAdmin = userdataServerAdmin;
    }

    public boolean isUserdataServerAdmin() {
        return userdataServerAdmin;
    }

    public boolean isServerAdmin() {
        return serverAdmin;
    }

    @Override
    public String toString() {
        return "LoginWithTokenResultDTO{" +
                "serverAdmin=" + serverAdmin +
                ", userdataServerAdmin=" + userdataServerAdmin +
                ", success='" + isSuccess() + '\'' +
                ", error='" + getErrorMessage() + '\'' +
                '}';
    }
}
