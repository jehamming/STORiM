package com.hamming.storim.interfaces;

import com.hamming.storim.model.dto.ThingDto;

public interface ThingListener {

    void thingAdded(ThingDto thing);
    void thingDeleted(ThingDto thing);
    void thingUpdated(ThingDto thing);
}
