package com.hamming.storim.factories;

import com.hamming.storim.Database;
import com.hamming.storim.model.Verb;
import com.hamming.storim.model.VerbOutput;
import com.hamming.storim.model.User;

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
        result.setCommand(cmd);
        result.setCaller(caller);
        return result;
    }


}
