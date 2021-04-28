package com.hamming.storim.common.view;

import com.hamming.storim.common.dto.*;

public interface GameView {
    void addPlayer(Long userId, String name, byte[] imageData);

    void scheduleAction(Action action);

    void addThing(ThingDto thingDto);

    void deleteAvatar(Long playerId);

    void deleteThing(Long thingId);

    void removePlayer(Long playerId);

    void resetView();

    void setRoom(RoomDto room);

    void setTile(TileDto tile);

    void setPlayerLocation(Long playerId, int x, int y);

    void updatePlayer(UserDto user, AvatarDto avatar);

    void updateThing(ThingDto thingDto);

    RoomDto getRoom();
}
