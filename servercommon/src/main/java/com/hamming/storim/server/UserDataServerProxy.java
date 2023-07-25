package com.hamming.storim.server;

import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.ProtocolDTO;
import com.hamming.storim.common.dto.protocol.requestresponse.AddUserDto;
import com.hamming.storim.common.dto.protocol.request.DeleteUserDto;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.dto.protocol.dataserver.VerifyUserTokenRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.VerifyUserTokenResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.GetAvatarResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.GetAvatarsResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.avatar.*;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.GetThingsForUserResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.GetTilesForUserResponseDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.tile.*;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.ValidateUserRequestDTO;
import com.hamming.storim.server.common.dto.protocol.dataserver.user.ValidateUserResponseDTO;
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
            Logger.info(this, " Error :" + response.getErrorMessage());
        }
        return response.getTile();
    }

    public TileDto getTile(Long tileId) {
        GetTileRequestDTO getTileRequestDto = new GetTileRequestDTO(tileId);
        GetTileResponseDTO response = connection.sendReceive(getTileRequestDto, GetTileResponseDTO.class );
        if (!response.isSuccess()) {
            Logger.info(this, " Error :" + response.getErrorMessage());
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
            Logger.info(this, " Error :" + getVerbsResponseDTO.getErrorMessage());
        }
        return getVerbsResponseDTO.getVerb();
    }

    public VerbDto addVerb(UserDto creator, String name, String toCaller, String toLocation) {
        AddVerbRequestDto addVerbRequestDto = new AddVerbRequestDto(creator.getId(),name, toCaller, toLocation);
        AddVerbResponseDTO addVerbResponseDTO = connection.sendReceive(addVerbRequestDto, AddVerbResponseDTO.class );
        if (!addVerbResponseDTO.isSuccess()) {
            Logger.info(this, " Error :" + addVerbResponseDTO.getErrorMessage());
        }
        return addVerbResponseDTO.getVerb();
    }
    public boolean deleteVerb(Long verbID) {
        DeleteVerbRequestDto deleteVerbRequestDto = new DeleteVerbRequestDto(verbID);
        DeleteVerbResponseDTO deleteVerbResponseDTO = connection.sendReceive(deleteVerbRequestDto, DeleteVerbResponseDTO.class);
        if (!deleteVerbResponseDTO.isSuccess()) {
            Logger.info(this, " Error :" + deleteVerbResponseDTO.getErrorMessage());
        }
        return deleteVerbResponseDTO.isSuccess();
    }

    public VerbDto updateVerb(Long verbId, String name, String toCaller, String toLocation) {
        UpdateVerbRequestDto updateVerbRequestDto = new UpdateVerbRequestDto(verbId,name, toCaller, toLocation);
        UpdateVerbResponseDTO updateVerbResponseDTO = connection.sendReceive(updateVerbRequestDto, UpdateVerbResponseDTO.class );
        if (!updateVerbResponseDTO.isSuccess()) {
            Logger.info(this, " Error :" + updateVerbResponseDTO.getErrorMessage());
        }
        return updateVerbResponseDTO.getVerb();
    }

    public List<Long> getAvatars(Long userId) {
        GetAvatarsRequestDTO getAvatarsRequestDTO = new GetAvatarsRequestDTO(userId);
        GetAvatarsResponseDTO response = connection.sendReceive(getAvatarsRequestDTO, GetAvatarsResponseDTO.class);
        if (!response.isSuccess()) {
            Logger.info(this, " Error :" + response.getErrorMessage());
        }
        return response.getAvatars();
    }

    public AvatarDto addAvatar(UserDto creator, String name, byte[] imageData) {
        AddAvatarRequestDto addAvatarRequestDto = new AddAvatarRequestDto(creator.getId(), name, imageData);
        AddAvatarResponseDTO response = connection.sendReceive(addAvatarRequestDto, AddAvatarResponseDTO.class);
        if (!response.isSuccess()) {
            Logger.info(this, " Error :" + response.getErrorMessage());
        }

        return response.getAvatar();
    }

    public AvatarDto getAvatar(Long avatarId) {
        GetAvatarRequestDTO getAvatarRequestDTO = new GetAvatarRequestDTO(avatarId);
        GetAvatarResponseDTO response = connection.sendReceive(getAvatarRequestDTO, GetAvatarResponseDTO.class);
        if (!response.isSuccess()) {
            Logger.info(this, " Error :" + response.getErrorMessage());
        }
        return response.getAvatar();
    }

    public boolean deleteAvatar(Long avatarID) {
        DeleteAvatarRequestDTO deleteAvatarRequestDTO = new DeleteAvatarRequestDTO(avatarID);
        DeleteAvatarResponseDTO response = connection.sendReceive(deleteAvatarRequestDTO, DeleteAvatarResponseDTO.class);
        if (!response.isSuccess()) {
            Logger.info(this, " Error :" + response.getErrorMessage());
        }
        return response.isSuccess();
    }

    public AvatarDto updateAvatar(Long avatarId, String name, byte[] imageData) {
        UpdateAvatarRequestDto updateAvatarRequestDto = new UpdateAvatarRequestDto(avatarId, name, imageData);
        UpdateAvatarResponseDTO response = connection.sendReceive(updateAvatarRequestDto, UpdateAvatarResponseDTO.class);
        if (!response.isSuccess()) {
            Logger.info(this, " Error :" + response.getErrorMessage());
        }
        return response.getAvatar();
    }

    public AvatarDto setAvatar(Long avatarId, Long userId) {
        SetAvatarRequestDto setAvatarRequestDto = new SetAvatarRequestDto(avatarId, userId);
        SetAvatarResponseDto response = connection.sendReceive(setAvatarRequestDto, SetAvatarResponseDto.class);
        if (!response.isSuccess()) {
            Logger.info(this, " Error :" + response.getErrorMessage());
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
            Logger.info(this, " Error :" + response.getErrorMessage());
        }
        return response.getThing();

    }

    public ThingDto getThing(Long thingID) {
        GetThingRequestDTO getThingRequestDTO = new GetThingRequestDTO(thingID);
        GetThingResponseDTO response = connection.sendReceive(getThingRequestDTO, GetThingResponseDTO.class );
        if (!response.isSuccess()) {
            Logger.info(this, " Error :" + response.getErrorMessage());
        }
        return response.getThing();
    }

    public boolean deleteThing(Long thingId) {
        DeleteThingRequestDto deleteThingRequestDto = new DeleteThingRequestDto(thingId);
        DeleteThingResponseDTO deleteThingResponseDTO = connection.sendReceive(deleteThingRequestDto, DeleteThingResponseDTO.class);
        if (!deleteThingResponseDTO.isSuccess()) {
            Logger.info(this, " Error :" + deleteThingResponseDTO.getErrorMessage());
        }
        return deleteThingResponseDTO.isSuccess();
    }

    public ThingDto updateThing(Long id, String name, String description, float scale, int rotation, byte[] imageData) {
        UpdateThingRequestDto updateThingRequestDto = new UpdateThingRequestDto(id, name, description,scale, rotation, imageData);
        UpdateThingResponseDTO response = connection.sendReceive(updateThingRequestDto, UpdateThingResponseDTO.class);
        if (!response.isSuccess()) {
            Logger.info(this, " Error :" + response.getErrorMessage());
        }
        return response.getThing();
    }

    public LocationDto getLocation(Long objectId) {
        GetLocationDto getLocationDto = new GetLocationDto(objectId);
        GetLocationResponseDto response = connection.sendReceive(getLocationDto, GetLocationResponseDto.class);
        return response.getLocation();
    }

    public void setLocation(Long objectId, LocationDto locationDto) {
        SetLocationDto setLocationDto = new SetLocationDto(objectId, locationDto);
        connection.send(setLocationDto);
    }

    public void setLocation(Long id, int x, int y) {
        LocationDto locationDto = getLocation(id);
        if ( locationDto != null ) {
            locationDto.setX(x);
            locationDto.setY(y);
            setLocation(id, locationDto);
        }
    }

    public ValidateUserResponseDTO validateUser(ClientConnection sourceClientConnection, String username, String password) {
        UserDto user = null;
        ValidateUserRequestDTO validateUserRequestDTO = new ValidateUserRequestDTO(sourceClientConnection.getId(), username, password);
        ValidateUserResponseDTO validateUserResponseDTO = connection.sendReceive(validateUserRequestDTO, ValidateUserResponseDTO.class);
        return validateUserResponseDTO;
    }

    public UserDto verifyUserToken(String source, Long userId, String token) {
            UserDto verifiedUser = null;
            VerifyUserTokenRequestDTO dto = new VerifyUserTokenRequestDTO(source, userId, token);
            VerifyUserTokenResponseDTO response = connection.sendReceive(dto, VerifyUserTokenResponseDTO.class);
            if (response.isSuccess() ) {
                verifiedUser = response.getUser();
            } else {
                Logger.info(this, " Error :" + response.getErrorMessage());
            }
            return verifiedUser;
    }

    public String verifyAdmin(String adminPassword) {
        UserDto verifiedUser = null;
        VerifyAdminRequestDTO dto = new VerifyAdminRequestDTO(adminPassword);
        VerifyAdminResponseDTO response = connection.sendReceive(dto, VerifyAdminResponseDTO.class);
        return response.getErrorMessage();
    }

    public HashMap<Long, String> getAllUsers() {
        GetUsersRequestDTO requestDTO = new GetUsersRequestDTO();
        GetUsersResultDTO resultDTO = connection.sendReceive(requestDTO, GetUsersResultDTO.class);
        return resultDTO.getUsers();
    }

    public UserDto getUser(Long userID) {
        UserDto userDto = null;
        GetUserDTO getUserDTO = new GetUserDTO(userID);
        GetUserResultDTO resultDTO = connection.sendReceive(getUserDTO, GetUserResultDTO.class);
        if ( resultDTO.isSuccess() && resultDTO.getUser() != null ) {
            userDto = resultDTO.getUser();
        }
        return userDto;
    }

    public UserDto updateUser(Long id, String username, String password, String name, String email, Long avatarId) {
        UpdateUserDto request = new UpdateUserDto(id, username, password, name, email, avatarId);
        UpdateUserResultDTO resultDTO = connection.sendReceive(request, UpdateUserResultDTO.class);
        return resultDTO.getUser();
    }

    public UpdateUserResultDTO updateUser(UpdateUserDto updateUserDto) {
        UpdateUserResultDTO result = connection.sendReceive(updateUserDto,UpdateUserResultDTO.class );
        return  result;
    }


    public void deleteUser(DeleteUserDto deleteUserDto) {
        connection.send(deleteUserDto);
    }

    public AddUserResultDTO addUser(AddUserDto addUserDto) {
        AddUserResultDTO resultDTO = connection.sendReceive(addUserDto, AddUserResultDTO.class);
        return resultDTO;
    }
}
