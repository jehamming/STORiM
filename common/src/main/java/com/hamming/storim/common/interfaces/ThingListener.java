package com.hamming.storim.common.interfaces;

import com.hamming.storim.common.dto.ThingDto;

public interface ThingListener {

    void thingAdded(ThingDto thing);
    void thingDeleted(ThingDto thing);
    void thingUpdated(ThingDto thing);
}
