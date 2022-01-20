package com.hamming.storim.common.interfaces;

import com.hamming.storim.common.dto.VerbDto;

public interface VerbListener {

    public void verbReceived(VerbDto verb);
    public void verbDeleted(Long verbID);


}
