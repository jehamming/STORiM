package com.hamming.storim.common;

import com.hamming.storim.common.controllers.ConnectionController;
import com.hamming.storim.common.dto.*;
import com.hamming.storim.common.dto.protocol.request.*;
import com.hamming.storim.common.dto.protocol.requestresponse.*;
import com.hamming.storim.common.interfaces.Client;

import java.util.HashMap;
import java.util.List;

public class MicroServerProxy implements Client {
    private ConnectionController connectionController;
    private String id;

    public MicroServerProxy(ConnectionController connectionController) {
        this.connectionController = connectionController;
        this.id = getClass().getSimpleName();
    }

    public ConnectionController getConnectionController() {
        return connectionController;
    }

    public RoomDto getRoom(Long roomId) {
        GetRoomResultDTO response = connectionController.sendReceive(new GetRoomDTO(roomId), GetRoomResultDTO.class);
        return response.getRoom();
    }


    public HashMap<Long, String> getRooms() throws MicroServerException {
        HashMap<Long, String> result = new HashMap<>();
        GetRoomsResultDTO response = connectionController.sendReceive(new GetRoomsDTO(), GetRoomsResultDTO.class);
        if (response.isSuccess() && response.getRooms() != null) {
            result = response.getRooms();
        } else {
            throw new MicroServerException(response.getErrorMessage());
        }
        return result;
    }

    public HashMap<Long, String> getUsers() throws MicroServerException {
        HashMap<Long, String> returnVal;
        GetUsersResultDTO response = connectionController.sendReceive(new GetUsersRequestDTO(), GetUsersResultDTO.class);
        if (response.isSuccess()) {
            returnVal = response.getUsers();
        } else {
            throw new MicroServerException(response.getErrorMessage());
        }
        return returnVal;
    }

    public void addUser(String username, String password, String name, String email) throws MicroServerException {
        AddUserDto addUserDto = new AddUserDto(username, password, name, email);
        AddUserResultDTO response = connectionController.sendReceive(addUserDto, AddUserResultDTO.class);
        if (!response.isSuccess()) {
            throw new MicroServerException(response.getErrorMessage());
        }
    }

    public void updateUser(Long id, String username, String password, String name, String email) {
        UpdateUserDto updateUserDto = new UpdateUserDto(id, username, password, name, email, null);
        connectionController.send(updateUserDto);
    }

    public void deleteUser(Long id) {
        DeleteUserDto dto = new DeleteUserDto(id);
        connectionController.send(dto);
    }

    public UserDto getUser(Long userId) throws MicroServerException {
        UserDto userDto;
        GetUserDTO request = new GetUserDTO(userId);
        GetUserResultDTO response = connectionController.sendReceive(request, GetUserResultDTO.class);
        if (response.isSuccess()) {
            userDto = response.getUser();
        } else {
            throw new MicroServerException(response.getErrorMessage());
        }
        return userDto;
    }

    public ServerConfigurationDTO getServerConfiguration() throws MicroServerException {
        GetServerConfigDTO request = new GetServerConfigDTO();
        GetServerConfigResultDTO response = connectionController.sendReceive(request, GetServerConfigResultDTO.class);
        if (response.isSuccess()) {
            return response.getServerConfigurationDTO();
        } else {
            throw new MicroServerException(response.getErrorMessage());
        }
    }

    public void sendAuthorisationUpdate(Long id, String simpleName, List<Long> newEditorIds) {
        UpdateAuthorisationDto uaDto = new UpdateAuthorisationDto(id, simpleName, newEditorIds);
        connectionController.send(uaDto);
    }

    public HashMap<Long, String> findUsersByDisplayname(String searchText) throws MicroServerException {
        SearchUsersRequestDTO request = new SearchUsersRequestDTO(searchText);
        SearchUsersResultDTO result = connectionController.sendReceive(request, SearchUsersResultDTO.class);
        if (result.isSuccess()) {
            return result.getUsers();
        } else {
            throw new MicroServerException(result.getErrorMessage());
        }

    }

    public List<Long> getAvatars(Long userId) throws MicroServerException {
        List<Long> result;
        GetAvatarsDTO getAvatarsRequestDTO = new GetAvatarsDTO(userId);
        GetAvatarsResponseDTO response = connectionController.sendReceive(getAvatarsRequestDTO, GetAvatarsResponseDTO.class);
        if (response.isSuccess()) {
            result = response.getAvatars();
        } else {
            throw new MicroServerException(response.getErrorMessage());
        }
        return result;
    }

    public AvatarDto getAvatar(Long avatarId) throws MicroServerException {
        AvatarDto result = null;
        GetAvatarDTO getAvatarRequestDTO = new GetAvatarDTO(avatarId);
        GetAvatarResponseDTO response = connectionController.sendReceive(getAvatarRequestDTO, GetAvatarResponseDTO.class);
        if (response.isSuccess()) {
            result = response.getAvatar();
        } else {
            throw new MicroServerException(response.getErrorMessage());
        }
        return result;
    }

    public void setAvatar(Long avatarId) {
        SetAvatarDto setAvatarDto = new SetAvatarDto(avatarId);
        connectionController.send(setAvatarDto);
    }

    public void addAvatar(String avatarName, byte[] imgdata) {
        AddAvatarDto addAvatarDto = new AddAvatarDto(avatarName, imgdata);
        connectionController.send(addAvatarDto);
    }

    public void updateAvatar(Long avatarID, String avatarName, byte[] imgdata) {
        UpdateAvatarDto updateAvatarDto = new UpdateAvatarDto(avatarID, avatarName, imgdata);
        connectionController.send(updateAvatarDto);
    }

    public void deleteAvatar(Long avatarID) {
        DeleteAvatarDTO deleteAvatarDTO = new DeleteAvatarDTO(avatarID);
        connectionController.send(deleteAvatarDTO);
    }

    public void executeVerb(Long verbId, String txt) {
        connectionController.send(new ExecVerbDTO(verbId, txt));
    }

    public void addExit(String exitName, String toRoomURI, Long toRoomID, String exitDescription, Float exitScale, Integer exitRotation, byte[] imageData) {
        AddExitDto addExitDto = new AddExitDto(exitName, toRoomURI, toRoomID, exitDescription, exitScale, exitRotation, imageData);
        connectionController.send(addExitDto);
    }

    public void updateExit(Long thingId, String exitName, Long toRoomID, String toRoomURI, String exitDescription, Float exitScale, Integer exitRotation, byte[] imgdata) {
        UpdateExitDto updateExitDto = new UpdateExitDto(thingId, exitName, toRoomID, toRoomURI, exitDescription, exitScale, exitRotation, imgdata);
        connectionController.send(updateExitDto);
    }

    public void deleteExit(Long exitId) {
        DeleteExitDTO deleteExitDTO = new DeleteExitDTO(exitId);
        connectionController.send(deleteExitDTO);
    }

    public TileSetDto getTileSet(Long tileSetId) throws MicroServerException {
        TileSetDto tileSetDto = null;
        GetTileSetResultDTO response = connectionController.sendReceive(new GetTileSetDTO(tileSetId), GetTileSetResultDTO.class);
        if (response.isSuccess()) {
            tileSetDto = response.getTileSetDto();
        } else {
            throw new MicroServerException(response.getErrorMessage());
        }
        return tileSetDto;
    }

    public void updateThingLocation(Long thingId, int x, int y) {
        UpdateThingLocationDto updateThingLocationDto = new UpdateThingLocationDto(thingId, x, y);
        connectionController.send(updateThingLocationDto);
    }

    public void useExit(Long exitId) {
        connectionController.send(new UseExitRequestDTO(exitId));
    }

    public void updateExitLocation(Long exitID, Long roomId, int x, int y) {
        UpdateExitLocationDto updateExitLocationDto = new UpdateExitLocationDto(exitID, roomId, x, y);
        connectionController.send(updateExitLocationDto);
    }

    public void connect(String serverip, int port) throws MicroServerException {
        try {
            if ( connectionController.isConnected()) {
                // Do a silent disconnect.
                // Reason : connection code is async, so disconnect events can arrive later than connect events
                // For now I am uninterested in the previous connection.
                connectionController.disconnect(true);
            }
            connectionController.connect(this, serverip, port);
        } catch (Exception e) {
            throw new MicroServerException(e.getMessage());
        }
    }

    public void disconnect() {
        connectionController.disconnect(false);
    }


    public LoginResultDTO login(String username, String password, Long roomId) throws MicroServerException {
        LoginDTO loginDTO = ProtocolHandler.getInstance().getLoginDTO(username, password, roomId);
        LoginResultDTO response = connectionController.sendReceive(loginDTO, LoginResultDTO.class);
        if (!response.isSuccess()) {
            throw new MicroServerException(response.getErrorMessage());
        }
        return response;

    }

    public LoginWithTokenResultDTO loginWithToken(Long userID, String token, Long roomId) throws MicroServerException {
        LoginWithTokenDTO loginWithTokenDTO = ProtocolHandler.getInstance().getConnectDTO(userID, token, roomId);
        LoginWithTokenResultDTO response = connectionController.sendReceive(loginWithTokenDTO, LoginWithTokenResultDTO.class);
        if (!response.isSuccess()) {
            throw new MicroServerException(response.getErrorMessage());
        }
        return response;
    }

    public HashMap<Long, String> getRoomsForUser(Long userId) throws MicroServerException {
        HashMap<Long, String> rooms = null;
        GetRoomsForUserResponseDTO response = connectionController.sendReceive(new GetRoomsForUserDTO(userId), GetRoomsForUserResponseDTO.class);
        if (response.isSuccess()) {
            rooms = response.getRooms();
        } else {
            throw new MicroServerException(response.getErrorMessage());
        }
        return rooms;

    }

    public void teleport(Long userId, Long roomId) {
        TeleportRequestDTO teleportRequestDTO = new TeleportRequestDTO(userId, roomId);
        connectionController.send(teleportRequestDTO);
    }

    public void deleteRoom(Long roomId) {
        DeleteRoomDTO deleteRoomDTO = new DeleteRoomDTO(roomId);
        connectionController.send(deleteRoomDTO);
    }

    public void addRoom(String roomName, int rows, int cols) {
        AddRoomDto addRoomDto = new AddRoomDto(roomName, rows, cols);
        connectionController.send(addRoomDto);
    }

    public void updateRoom(Long roomId, String roomName, int rows, int cols, Long backTileSetId, int[][] backTileMap, Long frontTileSetId, int[][] frontTileMap) {
        UpdateRoomDto updateRoomDto = new UpdateRoomDto(roomId, roomName, rows, cols, backTileSetId, backTileMap, frontTileSetId, frontTileMap);
        connectionController.send(updateRoomDto);
    }

    public List<Long> getTileSets() throws MicroServerException {
        List<Long> tileSets = null;
        GetTileSetsDTO getTileSetsDTO = new GetTileSetsDTO();
        GetTileSetsResponseDTO response = connectionController.sendReceive(getTileSetsDTO, GetTileSetsResponseDTO.class);
        if (response.isSuccess()) {
            tileSets = response.getTileSets();
        } else {
            throw new MicroServerException(response.getErrorMessage());
        }
        return tileSets;
    }

    public List<Long> getThingsForUser(Long userId) throws MicroServerException {
        List<Long> thingIds = null;
        GetThingsForUserResponseDTO response = connectionController.sendReceive(new GetThingsForUserDTO(userId), GetThingsForUserResponseDTO.class);
        if (response.isSuccess()) {
            thingIds = response.getThings();
        } else {
            throw new MicroServerException(response.getErrorMessage());
        }
        return thingIds;
    }

    public ThingDto getThing(Long thingId) throws MicroServerException {
        ThingDto thingDto = null;
        GetThingResultDTO response = connectionController.sendReceive(new GetThingDTO(thingId), GetThingResultDTO.class);
        if (response.isSuccess()) {
            thingDto = response.getThing();
        } else {
            throw new MicroServerException(response.getErrorMessage());
        }
        return thingDto;
    }

    public void placeThingInRoom(Long thingID, Long roomId) {
        PlaceThingInRoomDTO placeThingInRoomDTO = new PlaceThingInRoomDTO(thingID, roomId);
        connectionController.send(placeThingInRoomDTO);
    }

    public void addThing(String thingName, String thingDescription, Float thingScale, Integer thingRotation, byte[] imageData) {
        AddThingDto addThingDto = new AddThingDto(thingName, thingDescription, thingScale, thingRotation, imageData);
        connectionController.send(addThingDto);
    }

    public void updateThing(Long thingId, String thingName, String thingDescription, Float thingScale, Integer thingRotation, byte[] imgdata) {
        UpdateThingDto updateThingDto = new UpdateThingDto(thingId, thingName, thingDescription, thingScale, thingRotation, imgdata);
        connectionController.send(updateThingDto);
    }

    public void deleteThing(Long thingID) {
        DeleteThingDTO deleteThingDTO = new DeleteThingDTO(thingID);
        connectionController.send(deleteThingDTO);
    }

    public List<Long> getTileSetsForUser(Long userId) throws MicroServerException {
        List<Long> tileSetIds;
        GetTileSetsForUserDTO getTileSetsForUserDTO = new GetTileSetsForUserDTO(userId);
        GetTileSetsForUserResponseDTO response = connectionController.sendReceive(getTileSetsForUserDTO, GetTileSetsForUserResponseDTO.class);
        if (response.isSuccess()) {
            tileSetIds = response.getTileSets();
        } else {
            throw new MicroServerException(response.getErrorMessage());
        }
        return tileSetIds;
    }

    public void deleteTileSet(Long tileSetId) {
        DeleteTileSetDTO deleteTileSetDTO = new DeleteTileSetDTO(tileSetId);
        connectionController.send(deleteTileSetDTO);
    }

    public void addTileSet(String roomName, int tileWidth, int tileHeight, byte[] tileSetImageData) {
        AddTileSetDto addTileSetDto = new AddTileSetDto(roomName, tileWidth, tileHeight, tileSetImageData);
        connectionController.send(addTileSetDto);

    }

    public void updateTileSet(Long id, String roomName, int tileWidth, int tileHeight, byte[] tileSetImageData) {
        UpdateTileSetDto updateTileSetDto = new UpdateTileSetDto(id, roomName, tileWidth, tileHeight, tileSetImageData);
        connectionController.send(updateTileSetDto);
    }

    public VerbDetailsDTO getVerb(Long verbId) throws MicroServerException {
        VerbDetailsDTO verbDetailsDTO = null;
        GetVerbDTO getVerbDTO = new GetVerbDTO(verbId);
        GetVerbResponseDTO response = connectionController.sendReceive(getVerbDTO, GetVerbResponseDTO.class);
        if (response.isSuccess()) {
            verbDetailsDTO = response.getVerb();
        } else {
            throw new MicroServerException(response.getErrorMessage());
        }
        return verbDetailsDTO;
    }

    public void addVerb(String name, String toCaller, String toLocation) {
        AddVerbDto addVerbDto = ProtocolHandler.getInstance().getAddVerbDTO(name, toCaller, toLocation);
        connectionController.send(addVerbDto);
    }

    public void updateVerb(Long verbId, String name, String toCaller, String toLocation) {
        UpdateVerbDto updateVerbDto = ProtocolHandler.getInstance().getUpdateVerbDTO(verbId, name, toCaller, toLocation);
        connectionController.send(updateVerbDto);
    }

    public void deleteVerb(Long verbId) {
        DeleteVerbDTO deleteVerbDTO = new DeleteVerbDTO(verbId);
        connectionController.send(deleteVerbDTO);
    }

    public void updateServerConfiguration(Long defaultTileSetId, int defaultTile, Long defaultRoomId, List<Long> serverAdmins) {
        UpdateServerConfigurationDTO dto = new UpdateServerConfigurationDTO(defaultTileSetId, defaultTile, defaultRoomId, serverAdmins);
        connectionController.send(dto);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
