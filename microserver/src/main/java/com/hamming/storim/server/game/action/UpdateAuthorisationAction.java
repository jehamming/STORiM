package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.protocol.ErrorDTO;
import com.hamming.storim.common.dto.protocol.request.UpdateAuthorisationDto;
import com.hamming.storim.common.dto.protocol.serverpush.TileSetUpdatedDTO;
import com.hamming.storim.server.Database;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.model.BasicObject;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.common.model.TileSet;

import java.util.HashMap;
import java.util.List;

public class UpdateAuthorisationAction extends Action<UpdateAuthorisationDto> {

    private HashMap<String, Class> registeredBasicObjects;

    public UpdateAuthorisationAction(STORIMClientConnection client) {
        super(client);
        registeredBasicObjects = new HashMap<>();
        registeredBasicObjects();
    }

    private void registeredBasicObjects() {
        registeredBasicObjects.put(TileSetDto.class.getSimpleName(), TileSet.class);
        registeredBasicObjects.put(RoomDto.class.getSimpleName(), Room.class);
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        UpdateAuthorisationDto dto = getDto();
        Class basicObjectClass = registeredBasicObjects.get(dto.getDtoClassName());
        if (basicObjectClass != null) {
            BasicObject basicObject = Database.getInstance().findById(basicObjectClass, dto.getId());
            if (basicObject != null) {
                if (client.isAuthorized(basicObject)) {
                    List<Long> oldEditors = basicObject.getEditors();
                    basicObject.setEditors(dto.getNewEditors());
                    client.getServer().getAuthorisationController().fireAuthorisationChanged(basicObject, oldEditors);
                } else {
                    ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "UnAuthorized");
                    client.send(errorDTO);
                }
            } else {
                ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "Cannot find a " + dto.getDtoClassName() + " with id :" + getDto().getId());
                client.send(errorDTO);
            }
        } else {
            ErrorDTO errorDTO = new ErrorDTO(getClass().getSimpleName(), "Cannot find a BasicObject class for :" + getDto().getDtoClassName());
            client.send(errorDTO);
        }
    }

}
