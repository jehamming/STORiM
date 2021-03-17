package com.hamming.storim.controllers;

import com.hamming.storim.Controllers;
import com.hamming.storim.game.ProtocolHandler;
import com.hamming.storim.interfaces.RoomUpdateListener;
import com.hamming.storim.interfaces.ThingListener;
import com.hamming.storim.interfaces.UserListener;
import com.hamming.storim.model.dto.AvatarDto;
import com.hamming.storim.model.dto.ThingDto;
import com.hamming.storim.model.dto.ThingDto;
import com.hamming.storim.model.dto.protocol.avatar.DeleteAvatarDTO;
import com.hamming.storim.model.dto.protocol.thing.*;
import com.hamming.storim.net.NetCommandReceiver;
import com.hamming.storim.util.ImageUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThingController {

    private ProtocolHandler protocolHandler;
    private Controllers controllers;
    private List<ThingListener> thingListeners;
    private Map<Long, ThingDto> thingStore;


    public ThingController(Controllers controllers) {
        this.controllers = controllers;
        protocolHandler = new ProtocolHandler();
        thingListeners = new ArrayList<>();
        thingStore = new HashMap<>();
        controllers.getConnectionController().registerReceiver(ThingAddedDTO.class, (NetCommandReceiver<ThingAddedDTO>) dto -> {handleThingAdded(dto);});
        controllers.getConnectionController().registerReceiver(GetThingResultDTO.class, (NetCommandReceiver<GetThingResultDTO>) dto -> {handleGetThingResult(dto);});
        controllers.getConnectionController().registerReceiver(ThingDeletedDTO.class, (NetCommandReceiver<ThingDeletedDTO>) dto -> {handleThingDeleted(dto);});
        controllers.getConnectionController().registerReceiver(ThingUpdatedDTO.class, (NetCommandReceiver<ThingUpdatedDTO>) dto -> {handleThingUpdated(dto);});
    }


    private void handleThingUpdated(ThingUpdatedDTO dto) {
        ThingDto thing = dto.getThing();
        if ( thing != null ) {
            addToThingStore(thing);
            for (ThingListener l : thingListeners) {
                l.thingUpdated(thing);
            }
        }
    }

    private void handleThingDeleted(ThingDeletedDTO dto) {
        ThingDto thing  = removeFromThingStore(dto.getThingId());
        if (thing != null) {
            for (ThingListener l : thingListeners) {
                l.thingDeleted(thing);
            }
        }
    }

    private void handleGetThingResult(GetThingResultDTO dto) {
        addToThingStore(dto.getThing());
    }

    private void handleThingAdded(ThingAddedDTO dto) {
        for (ThingListener l : thingListeners) {
            l.thingAdded(dto.getThing());
        }
    }


    public void addThingListener(ThingListener l) {
        thingListeners.add(l);
    }


    public void addThingRequest(String name, String description, Float scale, int rotation, Image image) {
        AddThingDto addThingDto = protocolHandler.getAddThingDTO(name, description, scale, rotation, ImageUtils.encode(image));
        controllers.getConnectionController().send(addThingDto);
    }


    private ThingDto getFromThingStore(Long thingID) {
        return thingStore.get(thingID);
    }

    private void addToThingStore(ThingDto thing) {
        thingStore.put(thing.getId(), thing);
    }

    private ThingDto removeFromThingStore(Long thingId) {
        return thingStore.remove(thingId);
    }


    public List<ThingDto> getThings(Long userId) {
        List<ThingDto> things = new ArrayList<>();
        for (ThingDto thing : thingStore.values() ) {
            if ( thing.getOwnerID().equals(userId)) {
                things.add(thing);
            }
        }
        return things;
    }

    public void deleteThingRequest(Long thingID) {
        DeleteThingDTO dto = new DeleteThingDTO(thingID);
        controllers.getConnectionController().send(dto);
    }

    public void updateThingRequest(Long id, String name, String description, Float scale, int rotation, Image image) {
        UpdateThingDto dto = new UpdateThingDto(id, name, description, scale, rotation, ImageUtils.encode(image));
        controllers.getConnectionController().send(dto);
    }

    public void placeThingInRoomRequest(Long thingID, Long roomId) {
        PlaceThingInRoomRequestDTO dto = new PlaceThingInRoomRequestDTO(thingID, roomId);
        controllers.getConnectionController().send(dto);
    }
}
