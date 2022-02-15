package com.hamming.storim.common.controllers;

public class UserController  {
//
//    private ConnectionController connectionController;
//    private List<UserListener> userListeners;
//    private List<UserDto> users;
//    private Map<Long, LocationDto> userLocations;
//    private UserDto currentUser;
//    private ServerRegistrationDTO currentServer;
//    private Map<Long, AvatarDto> avatarStore;
//    private String token = null;
//
//    public UserController(ConnectionController connectionController) {
//        this.connectionController = connectionController;
//        connectionController.addConnectionListener(this);
//        userListeners = new ArrayList<UserListener>();
//        users = new ArrayList<UserDto>();
//        userLocations = new HashMap<Long, LocationDto>();
//        avatarStore = new HashMap<>();
//  }
//
//    private void handleAvatarUpdatedDTO(AvatarUpdatedDTO dto) {
//        AvatarDto avatar = dto.getAvatar();
//        if ( avatar != null ) {
//            addToAvatarStore(avatar);
//            for (UserListener l : userListeners) {
//                l.avatarUpdated(avatar);
//            }
//        }
//    }
//
//    private void handleGetAvatarResultDTO(GetAvatarResponseDTO dto) {
//        addToAvatarStore(dto.getAvatar());
//    }
//
//    private void handleAvatarDeletedDTO(AvatarDeletedDTO dto) {
//        AvatarDto avatar = removeFromAvatarStore(dto.getAvatarId());
//        if (avatar != null) {
//            for (UserListener l : userListeners) {
//                l.avatarDeleted(avatar);
//            }
//        }
//    }
//
//    private void handleAvatarAddedDTO(AvatarAddedDTO dto) {
//        addToAvatarStore(dto.getAvatar());
//        for (UserListener l : userListeners) {
//            l.avatarAdded(dto.getAvatar());
//        }
//    }
//
//    private AvatarDto getFromAvatarStore(Long avatarID) {
//        return avatarStore.get(avatarID);
//    }
//
//    private void addToAvatarStore(AvatarDto avatar) {
//        avatarStore.put(avatar.getId(), avatar);
//    }
//
//    private AvatarDto removeFromAvatarStore(Long avatarId) {
//        return avatarStore.remove(avatarId);
//    }
//
//    private void userUpdated(UserUpdatedDTO dto) {
//        users.remove(dto.getUser());
//        users.add(dto.getUser());
//        if (currentUser.getId().equals(dto.getUser().getId())) {
//            currentUser = dto.getUser();
//        }
//        for (UserListener l : userListeners) {
//            l.userUpdated(dto.getUser());
//        }
//    }
//
//    public AvatarDto getAvatar(Long avatarID) {
//        return getFromAvatarStore(avatarID);
//    }
//
//    public List<AvatarDto> getCurrentUserAvatars() {
//        List<AvatarDto> avatars = new ArrayList<>();
//        for (AvatarDto avatar : avatarStore.values() ) {
//            if ( avatar.getOwnerID().equals(currentUser.getId())) {
//                avatars.add(avatar);
//            }
//        }
//        return avatars;
//    }
//
//
//
//    private void connectResult(ConnectResultDTO dto) {
//        if (dto.isConnectSucceeded()) {
//            if ( dto.getLocation() != null ) {
//                userLocations.put(dto.getUser().getId(), dto.getLocation());
//            }
//            fireUserConnectedEvent(dto.getUser());
//        }
//    }
//
//
//
//
//    public void addUserListener(UserListener l) {
//        userListeners.add(l);
//    }
//
//    public UserDto getCurrentUser() {
//        return currentUser;
//    }
//
//    public LocationDto getCurrentUserLocation() {
//        return userLocations.get(currentUser.getId());
//    }
//
//    private void handleGetUserResult(GetUserResultDTO dto) {
//        if (dto.isSuccess()) {
//            users.add(dto.getUser());
//        } else {
//            System.out.println("Get user failed: " + dto.getErrorMessage());
//        }
//    }
//    public void fireUserConnectedEvent(UserDto userDto) {
//        for (UserListener l : userListeners) {
//            l.userConnected(userDto);
//        }
//    }
//
//    private void userDisconnected(UserDisconnectedDTO dto) {
//        UserDto user = findUserById(dto.getUserID());
//        if (user != null) {
//            users.remove(user);
//            userLocations.remove(user.getId());
//            for (UserListener l : userListeners) {
//                l.userDisconnected(user);
//            }
//        }
//    }
//
//    public void setUserLocation(Long userId, LocationDto loc) {
//        userLocations.put(userId, loc);
//    }
//
//    public void setCurrentUserLocation(LocationDto loc) {
//        userLocations.put(currentUser.getId(), loc);
//    }
//
//    public LocationDto getUserLocation(Long userId) {
//        return userLocations.get(userId);
//    }
//
//    public UserDto findUserById(Long userId) {
//        UserDto found = null;
//        for (UserDto user : users) {
//            if (user.getId().equals(userId)) {
//                found = user;
//                break;
//            }
//        }
//        return found;
//    }
//
//
//    @Override
//    public void connected() {
//
//    }
//
//    @Override
//    public void disconnected() {
//        users = new ArrayList<UserDto>();
//        userLocations = new HashMap<Long, LocationDto>();
//        avatarStore = new HashMap<>();
//    }
//
//    public void deleteAvatar(Long avatarID) {
//        DeleteAvatarDTO deleteAvatarDTO = new DeleteAvatarDTO(avatarID);
//        connectionController.send(deleteAvatarDTO);
//    }
//
//    public void addAvatarRequest(String avatarName, byte[] avatarImage) {
//        AddAvatarDto addAvatarDto = ProtocolHandler.getInstance().getAddAvatarDTO(avatarName, avatarImage);
//        connectionController.send(addAvatarDto);
//    }
//
//    public void updateAvatarRequest(Long avatarID, String avatarName, byte[] avatarImage) {
//        UpdateAvatarDto updateAvatarDto = new UpdateAvatarDto(avatarID, avatarName, avatarImage);
//        connectionController.send(updateAvatarDto);
//    }
//
//    public void setAvatarRequest(AvatarDto avatar) {
//        UpdateUserDto updateUserDto = new UpdateUserDto(currentUser.getId(), null, null, avatar.getId());
//        connectionController.send(updateUserDto);
//    }
}
