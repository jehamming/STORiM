package com.hamming.storim.client.engine.actions;

import com.hamming.storim.client.ImageUtils;
import com.hamming.storim.client.view.GameView;
import com.hamming.storim.client.view.Player;
import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.UserDto;

import java.awt.*;


public class UpdatePlayerAction implements Action {

    private GameView viewer;
    private UserDto user;
    private AvatarDto avatar;


    public UpdatePlayerAction(GameView viewer, UserDto user, AvatarDto avatar) {
        this.viewer = viewer;
        this.user = user;
        this.avatar = avatar;
    }

    @Override
    public void execute() {
        Player player = viewer.getPlayer(user.getId());
        if (player != null ) {
            player.setDisplayName(user.getName());
            if ( avatar != null ) {
                Image image = ImageUtils.decode(avatar.getImageData());
                int roomSize = 10;
                int widthPerTile = viewer.getWidth() / roomSize;
                image = ImageUtils.resize(image, widthPerTile, widthPerTile);
                player.setImage(image);
            } else {
                player.setImage(viewer.getDefaultUserImage());
            }
        }
    }

    @Override
    public String toString() {
        return "UpdatePlayerAction{" +
                "user=" + user +
                ", avatar=" + avatar +
                '}';
    }
}
