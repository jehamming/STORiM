package com.hamming.storim.server.common.factories;

import com.hamming.storim.server.Database;
import com.hamming.storim.server.common.model.Verb;
import com.hamming.storim.server.common.model.VerbOutput;

public class VerbResultFactory {
    private static VerbResultFactory instance;

    private VerbResultFactory() { };

    public static VerbResultFactory getInstance() {
        if ( instance == null ) {
            instance = new VerbResultFactory();
        }
        return instance;
    }

    public VerbOutput createCommandResult(Verb cmd, Long callerId) {
        Long id = Database.getInstance().getNextID();
        VerbOutput result = new VerbOutput();
        result.setId(id);
        result.setVerb(cmd);
        result.setCallerId(callerId);
        return result;
    }


}
