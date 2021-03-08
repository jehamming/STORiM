package com.hamming.storim.model.dto.protocol;

import com.hamming.storim.model.dto.DTO;

public class VersionCheckDTO implements DTO {


    private String clientVersion;
    private String serverVersion;
    private boolean versionCompatible = false;

    public VersionCheckDTO(String clientVersion){
        this.clientVersion = clientVersion;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public boolean isVersionCompatible() {
        return versionCompatible;
    }

    public void setVersionCompatible(boolean versionCompatible) {
        this.versionCompatible = versionCompatible;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    @Override
    public String toString() {
        return "VersionCheckDTO{" +
                "clientVersion='" + clientVersion + '\'' +
                ", serverVersion='" + serverVersion + '\'' +
                ", versionCompatible=" + versionCompatible +
                '}';
    }
}
