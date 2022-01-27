package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.request.ExecVerbDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.VerbFactory;
import com.hamming.storim.server.common.model.User;
import com.hamming.storim.server.common.model.Verb;
import com.hamming.storim.server.game.GameController;

public class ExecVerbAction extends Action<ExecVerbDTO> {
    private GameController controller;


    public ExecVerbAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.controller = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        User u = client.getCurrentUser();
        Verb cmd = VerbFactory.getInstance().findVerbByID(getDto().getCommandID());
        if (cmd != null ) {
            controller.executeVeb(getClient(), u, cmd, getDto().getInput());
        }
    }


}
