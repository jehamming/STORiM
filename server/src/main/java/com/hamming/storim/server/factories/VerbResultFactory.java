package com.hamming.storim.server.factories;

import com.hamming.storim.server.Database;
import com.hamming.storim.server.model.Verb;
import com.hamming.storim.server.model.VerbOutput;
import com.hamming.storim.server.model.User;

public class VerbResultFactory {
    private static VerbResultFactory instance;

    private VerbResultFactory() { };

    public static VerbResultFactory getInstance() {
        if ( instance == null ) {
            instance = new VerbResultFactory();
        }
        return instance;
    }

    public VerbOutput createCommandResult(Verb cmd, User caller) {
        Long id = Database.getInstance().getNextID();
        VerbOutput result = new VerbOutput(id);
        result.setVerb(cmd);
        result.setCaller(caller);
        return result;
    }


}
