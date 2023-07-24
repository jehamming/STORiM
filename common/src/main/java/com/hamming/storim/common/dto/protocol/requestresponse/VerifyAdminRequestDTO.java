package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class VerifyAdminRequestDTO extends ProtocolDTO {


    private String adminPassword;

    public VerifyAdminRequestDTO(String adminPassword){
        this.adminPassword = adminPassword;
    }

    public String getAdminPassword() {
        return adminPassword;
    }
}
