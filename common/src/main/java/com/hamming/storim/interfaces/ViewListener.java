package com.hamming.storim.interfaces;

import com.hamming.storim.model.dto.ThingDto;
import com.hamming.storim.model.dto.UserDto;

public interface ViewListener {
     void userSelectedInView(UserDto user);
     void thingSelectedInView(ThingDto thing);
}
