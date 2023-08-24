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

    public TileDto getTile(Long tileId) throws STORIMException {
        GetTileRequestDTO getTileRequestDto = new GetTileRequestDTO(tileId);
        GetTileResponseDTO response = connection.sendReceive(getTileRequestDto, GetTileResponseDTO.class );
        if (!response.isSuccess()) {
            throw new STORIMException(response.getErrorMessage());
        }
        return response.getTile();
    }

    public List<Long> getTilesForUser(Long userId) throws STORIMException {
        GetTilesForUserRequestDTO getTilesForUserRequestDto = new GetTilesForUserRequestDTO(userId);
        GetTilesForUserResponseDTO response = connection.sendReceive(getTilesForUserRequestDto, GetTilesForUserResponseDTO.class );
        if (!response.isSuccess()) {
            throw new STORIMException(response.getErrorMessage());
        }
        return response.getTiles();
    }

    public List<Long> getThingsForUser(Long userId) throws STORIMException {
        GetThingsForUserRequestDTO  getThingsForUserRequestDTO = new GetThingsForUserRequestDTO(userId);
        GetThingsForUserResponseDTO response = connection.sendReceive(getThingsForUserRequestDTO, GetThingsForUserResponseDTO.class );
        if ( !response.isSuccess() ) {
            throw new STORIMException(response.getErrorMessage());
        }
        return response.getThings();
    }

    public HashMap<Long, String> getVerbs(Long userId) throws STORIMException {
        GetVerbsResponseDTO getVerbsResponseDTO = connection.sendReceive(new GetVerbsRequestDTO(userId), GetVerbsResponseDTO.class );
        if ( !getVerbsResponseDTO.isSuccess()) {
            throw new STORIMException(getVerbsResponseDTO.getErrorMessage());
        }
        return getVerbsResponseDTO.getVerbs();
    }

    public VerbDetailsDTO getVerb(Long verbId) throws STORIMException {
        GetVerbDetailsResponseDTO getVerbsResponseDTO = connection.sendReceive(new GetVerbDetailsRequestDTO(verbId), GetVerbDetailsResponseDTO.class );
        if (!getVerbsResponseDTO.isSuccess()) {
            throw new STORIMException(getVerbsResponseDTO.getErrorMessage());
        }
        return getVerbsResponseDTO.getVerb();
    }

    public VerbDto addVerb(UserDto creator, String name, String toCaller, String toLocation) throws STORIMException {
        AddVerbRequestDto addVerbRequestDto = new AddVerbRequestDto(creator.getId(),name, toCaller, toLocation);
        AddVerbResponseDTO addVerbResponseDTO = connection.sendReceive(addVerbRequestDto, AddVerbResponseDTO.class );
        if (!addVerbResponseDTO.isSuccess()) {
            throw new STORIMException(addVerbResponseDTO.getErrorMessage());
        }
        return addVerbResponseDTO.getVerb();
    }
    public void deleteVerb(Long verbID) throws STORIMException {
        DeleteVerbRequestDto deleteVerbRequestDto = new DeleteVerbRequestDto(verbID);
        DeleteVerbResponseDTO deleteVerbResponseDTO = connection.sendReceive(deleteVerbRequestDto, DeleteVerbResponseDTO.class);
        if (!deleteVerbResponseDTO.isSuccess()) {
            throw new STORIMException(deleteVerbResponseDTO.getErrorMessage());
        }
    }

    public VerbDto updateVerb(Long verbId, String name, String toCaller, String toLocation) throws STORIMException {
        UpdateVerbRequestDto updateVerbRequestDto = new UpdateVerbRequestDto(verbId,name, toCaller, toLocation);
        UpdateVerbResponseDTO updateVerbResponseDTO = connection.sendReceive(updateVerbRequestDto, UpdateVerbResponseDTO.class );
        if (!updateVerbResponseDTO.isSuccess()) {
            throw new STORIMException(updateVerbResponseDTO.getErrorMessage());
        }
        return updateVerbResponseDTO.getVerb();
    }

    public List<Long> getAvatars(Long userId) throws STORIMException {
        GetAvatarsRequestDTO getAvatarsRequestDTO = new GetAvatarsRequestDTO(userId);
        GetAvatarsResponseDTO response = connection.sendReceive(getAvatarsRequestDTO, GetAvatarsResponseDTO.class);
        if (!response.isSuccess()) {
            throw new STORIMException(response.getErrorMessage());
        }
        return response.getAvatars();
    }

    public AvatarDto addAvatar(UserDto creator, String name, byte[] imageData) throws STORIMException {
        AddAvatarRequestDto addAvatarRequestDto = new AddAvatarRequestDto(creator.getId(), name, imageData);
        AddAvatarResponseDTO response = connection.sendReceive(addAvatarRequestDto, AddAvatarResponseDTO.class);
        if (!response.isSuccess()) {
            throw new STORIMException(response.getErrorMessage());
        }

        return response.getAvatar();
    }

    public AvatarDto getAvatar(Long avatarId) throws STORIMException {
        GetAvatarRequestDTO getAvatarRequestDTO = new GetAvatarRequestDTO(avatarId);
        GetAvatarResponseDTO response = connection.sendReceive(getAvatarRequestDTO, GetAvatarResponseDTO.class);
        if (!response.isSuccess()) {
            throw new STORIMException(response.getErrorMessage());
        }
        return response.getAvatar();
    }

    public void deleteAvatar(Long avatarID) throws STORIMException {
        DeleteAvatarRequestDTO deleteAvatarRequestDTO = new DeleteAvatarRequestDTO(avatarID);
        DeleteAvatarResponseDTO response = connection.sendReceive(deleteAvatarRequestDTO, DeleteAvatarResponseDTO.class);
        if (!response.isSuccess()) {
            throw new STORIMException(response.getErrorMessage());
        }
    }

    public AvatarDto updateAvatar(Long avatarId, String name, byte[] imageData) throws STORIMException {
        UpdateAvatarRequestDto updateAvatarRequestDto = new UpdateAvatarRequestDto(avatarId, name, imageData);
        UpdateAvatarResponseDTO response = connection.sendReceive(updateAvatarRequestDto, UpdateAvatarResponseDTO.class);
        if (!response.isSuccess()) {
            throw new STORIMException(response.getErrorMessage());
        }
        return response.getAvatar();
    }

    public AvatarDto setAvatar(Long avatarId, Long userId) throws STORIMException {
        SetAvatarRequestDto setAvatarRequestDto = new SetAvatarRequestDto(avatarId, userId);
        SetAvatarResponseDto response = connection.sendReceive(setAvatarRequestDto, SetAvatarResponseDto.class);
        if (!response.isSuccess()) {
            throw new STORIMException(response.getErrorMessage());
        }
        return response.getAvatar();
    }

    public void send(ProtocolDTO dto) {
        connection.send(dto);
    }

    public ThingDto addThing(UserDto creator, String name, String description, float scale, int rotation, byte[] imageData) throws STORIMException {
        AddThingRequestDto addThingRequestDto = new AddThingRequestDto(creator.getId(), name, description, scale, rotation, imageData);
        AddThingResponseDTO response = connection.sendReceive(addThingRequestDto, AddThingResponseDTO.class);
        if (!response.isSuccess()) {
            throw new STORIMException(response.getErrorMessage());
        }
        return response.getThing();

    }

    public ThingDto getThing(Long thingID) throws STORIMException {
        GetThingRequestDTO getThingRequestDTO = new GetThingRequestDTO(thingID);
        GetThingResponseDTO response = connection.sendReceive(getThingRequestDTO, GetThingResponseDTO.class );
        if (!response.isSuccess()) {
            throw new STORIMException(response.getErrorMessage());
        }
        return response.getThing();
    }

    public void deleteThing(Long thingId) throws STORIMException {
        DeleteThingRequestDto deleteThingRequestDto = new DeleteThingRequestDto(thingId);
        DeleteThingResponseDTO deleteThingResponseDTO = connection.sendReceive(deleteThingRequestDto, DeleteThingResponseDTO.class);
        if (!deleteThingResponseDTO.isSuccess()) {
            throw new STORIMException(deleteThingResponseDTO.getErrorMessage());
        }
    }

    public ThingDto updateThing(Long id, String name, String description, float scale, int rotation, byte[] imageData) throws STORIMException {
        UpdateThingRequestDto updateThingRequestDto = new UpdateThingRequestDto(id, name, description,scale, rotation, imageData);
        UpdateThingResponseDTO response = connection.sendReceive(updateThingRequestDto, UpdateThingResponseDTO.class);
        if (!response.isSuccess()) {
            throw new STORIMException(response.getErrorMessage());
        }
        return response.getThing();
    }

    public LocationDto getLocation(Long objectId) throws STORIMException {
        GetLocationDto getLocationDto = new GetLocationDto(objectId);
        GetLocationResponseDto response = connection.sendReceive(getLocationDto, GetLocationResponseDto.class);
        if (!response.isSuccess()) {
            throw new STORIMException(response.getErrorMessage());
        }
        return response.getLocation();
    }

    public void setLocation(Long objectId, LocationDto locationDto) {
        SetLocationDto setLocationDto = new SetLocationDto(objectId, locationDto);
        connection.send(setLocationDto);
    }

    public void setLocation(Long id, int x, int y) throws STORIMException {
        LocationDto locationDto = getLocation(id);
        if ( locationDto != null ) {
            locationDto.setX(x);
            locationDto.setY(y);
            setLocation(id, locationDto);
        }
    }

    public ValidateUserResponseDTO validateUser(ClientConnection sourceClientConnection, String username, String password) throws STORIMException {
        UserDto user = null;
        ValidateUserRequestDTO validateUserRequestDTO = new ValidateUserRequestDTO(sourceClientConnection.getId(), username, password);
        ValidateUserResponseDTO validateUserResponseDTO = connection.sendReceive(validateUserRequestDTO, ValidateUserResponseDTO.class);
        if (!validateUserResponseDTO.isSuccess())  {
            throw new STORIMException(validateUserResponseDTO.getErrorMessage());
        }
        return validateUserResponseDTO;
    }

    public VerifyUserTokenResponseDTO verifyUserToken(String source, Long userId, String token) {
            UserDto verifiedUser = null;
            VerifyUserTokenRequestDTO dto = new VerifyUserTokenRequestDTO(source, userId, token);
            VerifyUserTokenResponseDTO response = connection.sendReceive(dto, VerifyUserTokenResponseDTO.class);
            return  response;
    }


    public HashMap<Long, String> getAllUsers() throws STORIMException {
        GetUsersRequestDTO requestDTO = new GetUsersRequestDTO();
        GetUsersResultDTO resultDTO = connection.sendReceive(requestDTO, GetUsersResultDTO.class);
        if (!resultDTO.isSuccess()) {
            throw new STORIMException(resultDTO.getErrorMessage());
        }
        return resultDTO.getUsers();
    }

    public HashMap<Long, String> searchUsers(String searchQuery) throws STORIMException {
        SearchUsersRequestDTO requestDTO = new SearchUsersRequestDTO(searchQuery);
        SearchUsersResultDTO resultDTO = connection.sendReceive(requestDTO, SearchUsersResultDTO.class);
        if (!resultDTO.isSuccess()) {
            throw new STORIMException(resultDTO.getErrorMessage());
        }
        return resultDTO.getUsers();
    }

    public UserDto getUser(Long userID) throws STORIMException {
        UserDto userDto = null;
        GetUserDTO getUserDTO = new GetUserDTO(userID);
        GetUserResultDTO resultDTO = connection.sendReceive(getUserDTO, GetUserResultDTO.class);
        if ( resultDTO.isSuccess() && resultDTO.getUser() != null ) {
            userDto = resultDTO.getUser();
        } else {
            if (!resultDTO.isSuccess()) {
                throw new STORIMException(resultDTO.getErrorMessage());
            }
            if ( resultDTO.getUser() == null ) {
                throw new STORIMException("No User found");
            }
        }
        return userDto;
    }

    public UserDto getUserByUsername(String userName) throws STORIMException {
        UserDto userDto = null;
        GetUserByUserNameDTO getUserDTO = new GetUserByUserNameDTO(userName);
        GetUserByUsernameResultDTO resultDTO = connection.sendReceive(getUserDTO, GetUserByUsernameResultDTO.class);
        if ( resultDTO.isSuccess() && resultDTO.getUser() != null ) {
            userDto = resultDTO.getUser();
        } else {
            if (!resultDTO.isSuccess()) {
                throw new STORIMException(resultDTO.getErrorMessage());
            }
            if ( resultDTO.getUser() == null ) {
                throw new STORIMException("No User found");
            }
        }
        return userDto;
    }

    public UserDto updateUser(Long id, String username, String password, String name, String email, Long avatarId) throws STORIMException {
        UpdateUserDto request = new UpdateUserDto(id, username, password, name, email, avatarId);
        UpdateUserResultDTO resultDTO = connection.sendReceive(request, UpdateUserResultDTO.class);
        if ( !resultDTO.isSuccess() ) {
            throw new STORIMException(resultDTO.getErrorMessage());
        }
        return resultDTO.getUser();
    }

    public UserDto updateUser(UpdateUserDto updateUserDto) throws STORIMException {
        UpdateUserResultDTO result = connection.sendReceive(updateUserDto,UpdateUserResultDTO.class );
        if ( !result.isSuccess() ) {
            throw new STORIMException(result.getErrorMessage());
        }
        return result.getUser();
    }


    public void deleteUser(DeleteUserDto deleteUserDto) {
        connection.send(deleteUserDto);
    }

    public UserDto addUser(AddUserDto addUserDto) throws STORIMException {
        AddUserResultDTO resultDTO = connection.sendReceive(addUserDto, AddUserResultDTO.class);
        if (!resultDTO.isSuccess()) {
            throw new STORIMException(resultDTO.getErrorMessage());
        }
        return resultDTO.getUserDto();
    }


}
