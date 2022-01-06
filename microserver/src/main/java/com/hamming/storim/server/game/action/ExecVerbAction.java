package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.verb.ExecVerbDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.VerbFactory;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.common.model.Verb;
import com.hamming.storim.server.game.GameController;

public class ExecVerbAction extends Action<ExecVerbDTO> {
    private GameController controller;
    private STORIMClientConnection client;

    public ExecVerbAction(GameController controller, STORIMClientConnection client) {

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
