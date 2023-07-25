package com.hamming.userdataserver.action;

import com.hamming.storim.common.dto.protocol.request.DeleteUserDto;
import com.hamming.storim.server.ServerWorker;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.DeleteAvatarRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.DeleteAvatarResponseDTO;
import com.hamming.storim.server.common.factories.LocationFactory;
import com.hamming.userdataserver.factories.AvatarFactory;
import com.hamming.userdataserver.factories.ThingFactory;
import com.hamming.userdataserver.factories.UserFactory;
import com.hamming.userdataserver.factories.VerbFactory;
import com.hamming.userdataserver.model.Avatar;
import com.hamming.userdataserver.model.Thing;
import com.hamming.userdataserver.model.User;
import com.hamming.userdataserver.model.Verb;

public class DeleteUserAction extends Action<DeleteUserDto> {
    private ServerWorker serverWorker;


    public DeleteUserAction(ServerWorker serverWorker, ClientConnection client) {
        super(client);
        this.serverWorker = serverWorker;
    }

    @Override
    public void execute() {
        //TODO Send events to all connections
        Long userId = getDto().getId();
        User user = UserFactory.getInstance().findUserById(userId);
        if ( user != null ) {
            // Delete verbs
            for (Verb verb :VerbFactory.getInstance().getVerbsFor(user.getId())) {
                VerbFactory.getInstance().deleteVerb(verb.getId());
            }
            // Delete Things
            for (Thing thing :ThingFactory.getInstance().getThings(user.getId())) {
                ThingFactory.getInstance().deleteThing(thing.getId());
            }
            // Remove location
            LocationFactory.getInstance().removeLocation(user.getId());

            // In the end; remove the user
            UserFactory.getInstance().deleteUser(user);
        }
    }

}
