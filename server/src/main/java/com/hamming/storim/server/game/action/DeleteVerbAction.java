package com.hamming.storim.server.game.action;

import com.hamming.storim.server.ClientConnection;
import com.hamming.storim.server.factories.VerbFactory;
import com.hamming.storim.server.game.GameController;
import com.hamming.storim.server.model.Verb;
import com.hamming.storim.common.dto.protocol.verb.DeleteVerbDTO;

public class DeleteVerbAction extends Action<DeleteVerbDTO> {
    private GameController controller;
    private ClientConnection client;

    public DeleteVerbAction(GameController controller, ClientConnection client) {
        this.controller = controller;
        this.client = client;
    }

    @Override
    public void execute() {
        Verb verb = VerbFactory.getInstance().findVerbByID(getDto().getVerbID());
        boolean success = VerbFactory.getInstance().deleteVerb(getDto().getVerbID());
        if (success) {
            controller.verbDeleted(verb);
        }
    }

}
