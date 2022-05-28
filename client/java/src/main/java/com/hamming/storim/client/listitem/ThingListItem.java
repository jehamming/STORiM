package com.hamming.storim.client.listitem;

import com.hamming.storim.common.dto.ThingDto;

public class ThingListItem {


    private ThingDto thing;

    public ThingListItem(ThingDto thing) {
        this.thing = thing;
    }

    public ThingDto getThing() {
        return thing;
    }

    public void setThing(ThingDto thing) {
        this.thing = thing;
    }

    @Override
    public String toString() {
        return thing.getName();
    }


}
