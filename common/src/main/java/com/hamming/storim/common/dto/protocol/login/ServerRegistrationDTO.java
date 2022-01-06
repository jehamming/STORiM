package com.hamming.storim.common.dto.protocol.login;

import com.hamming.storim.common.dto.protocol.ProtocolASyncRequestDTO;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class ServerRegistrationDTO extends ProtocolASyncRequestDTO {

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
}
