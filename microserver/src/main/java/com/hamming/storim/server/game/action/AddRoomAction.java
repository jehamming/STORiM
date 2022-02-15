package com.hamming.storim.server.game.action;

import com.hamming.storim.common.dto.TileDto;
import com.hamming.storim.common.dto.protocol.request.AddRoomDto;
import com.hamming.storim.server.STORIMClientConnection;
import com.hamming.storim.server.common.ClientConnection;
import com.hamming.storim.server.common.ImageUtils;
import com.hamming.storim.server.common.action.Action;
import com.hamming.storim.server.common.factories.RoomFactory;
import com.hamming.storim.server.common.model.Room;
import com.hamming.storim.server.game.GameController;

import java.awt.*;

public class AddRoomAction extends Action<AddRoomDto> {
    private GameController gameController;


    public AddRoomAction(GameController controller, STORIMClientConnection client) {
        super(client);
        this.gameController = controller;
    }

    @Override
    public void execute() {
        STORIMClientConnection client = (STORIMClientConnection) getClient();
        AddRoomDto dto = getDto();
        Long creator = client.getCurrentUser().getId();
        if (dto.getImageData() != null ) {
            addRoom(getClient(), creator, dto.getName(), dto.getImageData());
        } else if (dto.getTileId() != null ) {
            addRoom(getClient(), creator, dto.getName(), dto.getTileId());
        } else {
            addRoom(getClient(), creator, dto.getName());
        }
    }

    private void addRoom(ClientConnection source, Long creatorId, String name, Long tileID) {
//        // Create Tile
//        Tile tile = TileFactory.getInstance(STORIMMicroServer.DATADIR).findTileById(tileID);
//        addRoom(source, creatorId, name, tile);
    }

    private void addRoom(ClientConnection source, Long creatorId, String name, TileDto tile) {
        Room room = RoomFactory.getInstance().createRoom(creatorId, name);
        room.setOwnerId(creatorId);
        room.setTileId(tile.getId());
    }

    public void addRoom(ClientConnection source, Long creatorId, String name) {
        Room room = RoomFactory.getInstance().createRoom(creatorId, name);
        room.setOwnerId(creatorId);
    }

    public void addRoom(ClientConnection source, Long creatorId, String name, byte[] imageData) {
        Image image = ImageUtils.decode(imageData);
        //FIXME TIles
//        Tile tile = TileFactory.getInstance(STORIMMicroServer.DATADIR).createTile(creatorId, image);
//        tile.setCreatorId(creatorId);
//        tile.setOwnerId(creatorId);
//        addRoom(source, creatorId, name, tile);
    }


}
