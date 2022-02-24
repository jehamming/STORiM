package com.hamming.userdataserver.action;

import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.GetVerbsRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.GetVerbsResponseDTO;
import com.hamming.userdataserver.factories.VerbFactory;
import com.hamming.userdataserver.model.Verb;

import java.util.HashMap;
import java.util.List;

public class GetVerbsAction extends Action<GetVerbsRequestDTO> {

    private ServerWorker serverWorker;

    public GetVerbsAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        Long userId = getDto().getUserId();

        List<Verb> verbsForUser = VerbFactory.getInstance().getVerbsFor(userId);
        HashMap<Long, String> result = new HashMap<>();
        for (Verb v: verbsForUser) {
            result.put(v.getId(), v.getName());
        }

        GetVerbsResponseDTO getVerbsResponseDTO = new GetVerbsResponseDTO(result);
        getClient().send(getVerbsResponseDTO);
    }
}