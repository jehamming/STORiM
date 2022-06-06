package com.hamming.storim.server;

import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.*;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.*;
import com.hamming.storim.server.common.dto.protocol.dataserver.verb.*;

import java.util.HashMap;
import java.util.List;

public class UserDataServerProxy {
    private ClientConnection connection;

    public UserDataServerProxy(ClientConnection connection) {
        this.connection = connection;
    }

    public TileDto addTile(Long userId, byte[] imageData) {
        AddTileRequestDto addTileRequestDto = new AddTileRequestDto(userId, imageData);
        AddTileResponseDTO response = connection.sendReceive(addTileRequestDto, AddTileResponseDTO.class );
        if (!response.isSuccess()) {
            System.out.println("(" + getClass().getSimpleName() + ") Error :" + response.getErrorMessage());
        }
        return response.getTile();
    }

    public TileDto getTile(Long tileId) {
        GetTileRequestDTO getTileRequestDto = new GetTileRequestDTO(tileId);
        GetTileResponseDTO response = connection.sendReceive(getTileRequestDto, GetTileResponseDTO.class );
        if (!response.isSuccess()) {
            System.out.println("(" + getClass().getSimpleName() + ") Error :" + response.getErrorMessage());
        }
        return response.getTile();
    }

    public List<Long> getTilesForUser(Long userId) {
        GetTilesForUserRequestDTO getTilesForUserRequestDto = new GetTilesForUserRequestDTO(userId);
        GetTilesForUserResponseDTO response = connection.sendReceive(getTilesForUserRequestDto, GetTilesForUserResponseDTO.class );
        return response.getTiles();
    }

    public List<Long> getThingsForUser(Long userId) {
        GetThingsForUserRequestDTO  getThingsForUserRequestDTO = new GetThingsForUserRequestDTO(userId);
        GetThingsForUserResponseDTO response = connection.sendReceive(getThingsForUserRequestDTO, GetThingsForUserResponseDTO.class );
        return response.getThings();
    }

    public HashMap<Long, String> getVerbs(Long userId) {
        GetVerbsResponseDTO getVerbsResponseDTO = connection.sendReceive(new GetVerbsRequestDTO(userId), GetVerbsResponseDTO.class );
        return getVerbsResponseDTO.getVerbs();
    }

    public VerbDetailsDTO getVerb(Long verbId) {
        GetVerbDetailsResponseDTO getVerbsResponseDTO = connection.sendReceive(new GetVerbDetailsRequestDTO(verbId), GetVerbDetailsResponseDTO.class );
        if (!getVerbsResponseDTO.isSuccess()) {
            System.out.println("(" + getClass().getSimpleName() + ") Error :" + getVerbsResponseDTO.getErrorMessage());
        }
        return getVerbsResponseDTO.getVerb();
    }

    public VerbDto addVerb(UserDto creator, String name, String toCaller, String toLocation) {
        AddVerbRequestDto addVerbRequestDto = new AddVerbRequestDto(creator.getId(),name, toCaller, toLocation);
        AddVerbResponseDTO addVerbResponseDTO = connection.sendReceive(addVerbRequestDto, AddVerbResponseDTO.class );
        if (!addVerbResponseDTO.isSuccess()) {
            System.out.println("(" + getClass().getSimpleName() + ") Error :" + addVerbResponseDTO.getErrorMessage());
        }
        return addVerbResponseDTO.getVerb();
    }
    public boolean deleteVerb(Long verbID) {
        DeleteVerbRequestDto deleteVerbRequestDto = new DeleteVerbRequestDto(verbID);
        DeleteVerbResponseDTO deleteVerbResponseDTO = connection.sendReceive(deleteVerbRequestDto, DeleteVerbResponseDTO.class);
        if (!deleteVerbResponseDTO.isSuccess()) {
            System.out.println("(" + getClass().getSimpleName() + ") Error :" + deleteVerbResponseDTO.getErrorMessage());
        }
        return deleteVerbResponseDTO.isSuccess();
    }

    public VerbDto updateVerb(Long verbId, String name, String toCaller, String toLocation) {
        UpdateVerbRequestDto updateVerbRequestDto = new UpdateVerbRequestDto(verbId,name, toCaller, toLocation);
        UpdateVerbResponseDTO updateVerbResponseDTO = connection.sendReceive(updateVerbRequestDto, UpdateVerbResponseDTO.class );
        if (!updateVerbResponseDTO.isSuccess()) {
            System.out.println("(" + getClass().getSimpleName() + ") Error :" + updateVerbResponseDTO.getErrorMessage());
        }
        return updateVerbResponseDTO.getVerb();
    }

    public List<Long> getAvatars(Long userId) {
        GetAvatarsRequestDTO getAvatarsRequestDTO = new GetAvatarsRequestDTO(userId);
        GetAvatarsResponseDTO response = connection.sendReceive(getAvatarsRequestDTO, GetAvatarsResponseDTO.class);
        if (!response.isSuccess()) {
            System.out.println("(" + getClass().getSimpleName() + ") Error :" + response.getErrorMessage());
        }
        return response.getAvatars();
    }

    public AvatarDto addAvatar(UserDto creator, String name, byte[] imageData) {
        AddAvatarRequestDto addAvatarRequestDto = new AddAvatarRequestDto(creator.getId(), name, imageData);
        AddAvatarResponseDTO response = connection.sendReceive(addAvatarRequestDto, AddAvatarResponseDTO.class);
        if (!response.isSuccess()) {
            System.out.println("(" + getClass().getSimpleName() + ") Error :" + response.getErrorMessage());
        }

        return response.getAvatar();
    }

    public AvatarDto getAvatar(Long avatarId) {
        GetAvatarRequestDTO getAvatarRequestDTO = new GetAvatarRequestDTO(avatarId);
        GetAvatarResponseDTO response = connection.sendReceive(getAvatarRequestDTO, GetAvatarResponseDTO.class);
        if (!response.isSuccess()) {
            System.out.println("(" + getClass().getSimpleName() + ") Error :" + response.getErrorMessage());
        }
        return response.getAvatar();
    }

    public boolean deleteAvatar(Long avatarID) {
        DeleteAvatarRequestDTO deleteAvatarRequestDTO = new DeleteAvatarRequestDTO(avatarID);
        DeleteAvatarResponseDTO response = connection.sendReceive(deleteAvatarRequestDTO, DeleteAvatarResponseDTO.class);
        if (!response.isSuccess()) {
            System.out.println("(" + getClass().getSimpleName() + ") Error :" + response.getErrorMessage());
        }
        return response.isSuccess();
    }

    public AvatarDto updateAvatar(Long avatarId, String name, byte[] imageData) {
        UpdateAvatarRequestDto updateAvatarRequestDto = new UpdateAvatarRequestDto(avatarId, name, imageData);
        UpdateAvatarResponseDTO response = connection.sendReceive(updateAvatarRequestDto, UpdateAvatarResponseDTO.class);
        if (!response.isSuccess()) {
            System.out.println("(" + getClass().getSimpleName() + ") Error :" + response.getErrorMessage());
        }
        return response.getAvatar();
    }

    public AvatarDto setAvatar(Long avatarId, Long userId) {
        SetAvatarRequestDto setAvatarRequestDto = new SetAvatarRequestDto(avatarId, userId);
        SetAvatarResponseDto response = connection.sendReceive(setAvatarRequestDto, SetAvatarResponseDto.class);
        if (!response.isSuccess()) {
            System.out.println("(" + getClass().getSimpleName() + ") Error :" + response.getErrorMessage());
        }
        return response.getAvatar();
    }

    public void send(ProtocolDTO dto) {
        connection.send(dto);
    }

    public ThingDto addThing(UserDto creator, String name, String description, float scale, int rotation, byte[] imageData) {
        AddThingRequestDto addThingRequestDto = new AddThingRequestDto(creator.getId(), name, description, scale, rotation, imageData);
        AddThingResponseDTO response = connection.sendReceive(addThingRequestDto, AddThingResponseDTO.class);
        if (!response.isSuccess()) {
            System.out.println("(" + getClass().getSimpleName() + ") Error :" + response.getErrorMessage());
        }
        return response.getThing();

    }

    public ThingDto getThing(Long thingID) {
        GetThingRequestDTO getThingRequestDTO = new GetThingRequestDTO(thingID);
        GetThingResponseDTO response = connection.sendReceive(getThingRequestDTO, GetThingResponseDTO.class );
        if (!response.isSuccess()) {
            System.out.println("(" + getClass().getSimpleName() + ") Error :" + response.getErrorMessage());
        }
        return response.getThing();
    }
}
