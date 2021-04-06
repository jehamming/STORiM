package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.factories.VerbFactory;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.common.dto.protocol.verb.ExecVerbDTO;
import com.hamming.storim.server.model.User;
import com.hamming.storim.server.model.Verb;

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
