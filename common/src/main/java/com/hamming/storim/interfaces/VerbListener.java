package com.hamming.storim.interfaces;

import com.hamming.storim.model.dto.VerbDto;
import com.hamming.storim.model.dto.protocol.verb.ExecVerbResultDTO;

public interface VerbListener {

    public void verbReceived(VerbDto verb);
    public void verbDeleted(Long verbID);

    public void verbExecuted(ExecVerbResultDTO result);

}
