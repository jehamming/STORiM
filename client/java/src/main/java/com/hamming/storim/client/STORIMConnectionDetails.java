package com.hamming.storim.client;

import java.io.Serializable;

public class STORIMConnectionDetails implements Serializable {

    private String connectURL;
    private String username;
    private String password;

    public STORIMConnectionDetails(String url, String username, String password) {
        this.connectURL = url;
        this.username = username;
        this.password = password;
    }

    public String getConnectURL() {
        return connectURL;
    }

    public void setConnectURL(String connectURL) {
        this.connectURL = connectURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return connectURL +"-" + username;
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj instanceof STORIMConnectionDetails) {
            STORIMConnectionDetails d = (STORIMConnectionDetails) obj;
            return d.toString().equals(toString());
        } else {
            return super.equals(obj);
        }
    }
}
