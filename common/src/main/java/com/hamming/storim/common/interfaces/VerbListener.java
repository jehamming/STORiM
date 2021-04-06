package com.hamming.storim.common.interfaces;

import com.hamming.storim.common.dto.VerbDto;
import com.hamming.storim.common.dto.protocol.verb.ExecVerbResultDTO;

public interface VerbListener {

    public void verbReceived(VerbDto verb);
    public void verbDeleted(Long verbID);

    public void verbExecuted(ExecVerbResultDTO result);

}
