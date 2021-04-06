package com.hamming.storim.common.interfaces;

import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.UserDto;

public interface ViewListener {
     void userSelectedInView(UserDto user);
     void thingSelectedInView(ThingDto thing);
}
