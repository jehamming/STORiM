package com.hamming.storim.game.action;

import com.hamming.storim.ClientConnection;
import com.hamming.storim.factories.VerbFactory;
import com.hamming.storim.game.GameController;
import com.hamming.storim.model.*;
import com.hamming.storim.model.dto.protocol.ExecVerbDTO;

public class ExecVerbAction extends Action<ExecVerbDTO> {
    private GameController controller;
    private ClientConnection client;

    public ExecVerbAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        User u = client.getCurrentUser();
        execVerb(getDto().getCommandID(), getDto().getInput());
    }

    private void execVerb(long commandID, String message) {
        Verb cmd = VerbFactory.getInstance().findVerbByID(commandID);
        if (cmd != null ) {
            controller.executeVeb(client.getCurrentUser(), cmd, message);
        }
    }

}
