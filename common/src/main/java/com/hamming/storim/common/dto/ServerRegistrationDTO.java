package com.hamming.storim.common.dto;

import com.hamming.storim.common.dto.DTO;

public class ServerRegistrationDTO extends DTO {

        private final String serverName;
        private final String serverURL;
        private final int serverPort;

        public ServerRegistrationDTO(String serverName, String serverURL, int serverPort) {
                this.serverName = serverName;
                this.serverURL = serverURL;
                this.serverPort = serverPort;
        }

        public String getServerName() {
                return serverName;
        }

        public String getServerURL() {
                return serverURL;
        }

        public int getServerPort() {
                return serverPort;
        }

        @Override
        public String toString() {
                return "ServerRegistrationDTO{" +
                        "serverName='" + serverName + '\'' +
                        ", serverURL='" + serverURL + '\'' +
                        ", serverPort=" + serverPort +
                        '}';
        }
}
