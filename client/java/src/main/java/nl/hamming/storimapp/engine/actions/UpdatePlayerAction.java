package nl.hamming.storimapp.engine.actions;

import com.hamming.storim.model.dto.AvatarDto;
import com.hamming.storim.model.dto.UserDto;
import com.hamming.storim.util.ImageUtils;
import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Player;

import java.awt.*;
import java.awt.image.BufferedImage;


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
                Image image = avatar.getImage();
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
