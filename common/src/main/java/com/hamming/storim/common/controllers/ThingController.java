package com.hamming.storim.common.controllers;

import com.hamming.storim.common.Controllers;
import com.hamming.storim.common.ProtocolHandler;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.protocol.request.PlaceThingInRoomRequestDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateThingDto;
import com.hamming.storim.common.dto.protocol.requestresponse.AddThingDto;
import com.hamming.storim.common.dto.protocol.requestresponse.DeleteThingDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.GetThingResultDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.ThingAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.ThingDeletedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.old.ThingUpdatedDTO;
import com.hamming.storim.common.interfaces.ThingListener;
import com.hamming.storim.common.net.NetCommandReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThingController {

    private Controllers controllers;
    private List<ThingListener> thingListeners;
    private Map<Long, ThingDto> thingStore;


    public ThingController(Controllers controllers) {
        this.controllers = controllers;
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
        ThingDto thing  = removeFromThingStore(dto.getThing().getId());
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


    public void addThingRequest(String name, String description, Float scale, int rotation, byte[] image) {
        AddThingDto addThingDto = ProtocolHandler.getInstance().getAddThingDTO(name, description, scale, rotation, image);
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

    public void updateThingRequest(Long id, String name, String description, Float scale, int rotation, byte[] image) {
        UpdateThingDto dto = new UpdateThingDto(id, name, description, scale, rotation, image);
        controllers.getConnectionController().send(dto);
    }

    public void placeThingInRoomRequest(Long thingID, Long roomId) {
        PlaceThingInRoomRequestDTO dto = new PlaceThingInRoomRequestDTO(thingID, roomId);
        controllers.getConnectionController().send(dto);
    }

    public List<ThingDto> getThingsInRoom(Long roomId) {
        List<ThingDto> things = new ArrayList<>();
        for (ThingDto thing: thingStore.values()) {
            if (thing.getLocation().getRoomId().equals(roomId)) {
                things.add(thing);
            }
        }
        return things;
    }


    public ThingDto findThingById(Long id) {
        return getFromThingStore(id);
    }
}
