package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.protocol.verb.DeleteVerbDTO;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.VerbFactory;
import com.hamming.storim.server.common.model.Verb;
import com.hamming.storim.server.game.GameController;

public class DeleteVerbAction extends Action<DeleteVerbDTO> {
    private GameController controller;
    private STORIMClientConnection client;

    public DeleteVerbAction(GameController controller, STORIMClientConnection client) {

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