package com.hamming.storim.client;

import javax.swing.*;

public class STORIMRecentMenuItem extends JMenuItem {
    private STORIMConnectionDetails connectionDetails;

    public STORIMRecentMenuItem(STORIMConnectionDetails details)  {
        super( details.toString() );
        this.connectionDetails = details;
    }

    public STORIMConnectionDetails getConnectionDetails() {
        return connectionDetails;
    }
}
