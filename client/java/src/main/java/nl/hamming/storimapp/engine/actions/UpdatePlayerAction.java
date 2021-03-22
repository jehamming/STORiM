package nl.hamming.storimapp.engine.actions;

import com.hamming.storim.model.dto.AvatarDto;
import com.hamming.storim.model.dto.UserDto;
import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.Player;


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
                player.setImage(avatar.getImage());
            } else {
                player.setImage(null);
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
