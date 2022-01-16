package com.hamming.storim.common.view;

import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.UserDto;

public interface ViewListener {
     void userSelectedInView(UserDto user);
     void thingSelectedInView(ThingDto thing);
     void exitSelectedInView(ExitDto exit);
}
