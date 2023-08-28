package nl.hamming.storimapp.controllers;

import android.content.Context;

import com.hamming.storim.common.CalcTools;
import com.hamming.storim.common.MicroServerException;
import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.dto.AvatarDto;
import com.hamming.storim.common.dto.ExitDto;
import com.hamming.storim.common.dto.LocationDto;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.ThingDto;
import com.hamming.storim.common.dto.TileSetDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.MovementRequestDTO;
import com.hamming.storim.common.dto.protocol.serverpush.AvatarSetDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ExitAddedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ExitDeletedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ExitInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ExitLocationUpdatedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ExitUpdatedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.LocationUpdateDTO;
import com.hamming.storim.common.dto.protocol.serverpush.MessageInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.RoomUpdatedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ThingInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.ThingUpdatedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserDisconnectedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserEnteredRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserLeftRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserVerbsDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;
import com.hamming.storim.common.util.Logger;
import com.hamming.storim.common.view.ViewListener;

import java.util.ArrayList;
import java.util.List;

import nl.hamming.storimapp.STORIMClientApplication;
import nl.hamming.storimapp.ui.MainActivity;
import nl.hamming.storimapp.view.GameView;
import nl.hamming.storimapp.view.TileSet;

public class ChatController implements ConnectionListener {


    private UserDto currentUser;

    private RoomDto currentRoom;

    private MicroServerProxy microServerProxy;
    private MainActivity mainActivity;

    public ChatController(MainActivity mainActivity, MicroServerProxy microServerProxy) {
        this.microServerProxy = microServerProxy;
        this.mainActivity = mainActivity;
        microServerProxy.getConnectionController().addConnectionListener(this);
        registerReceivers();
    }

    private void registerReceivers() {
        microServerProxy.getConnectionController().registerReceiver(SetRoomDTO.class, (ProtocolReceiver<SetRoomDTO>) dto -> setRoom(dto.getRoom()));
        microServerProxy.getConnectionController().registerReceiver(UserInRoomDTO.class, (ProtocolReceiver<UserInRoomDTO>) dto -> userInRoom(dto));
        microServerProxy.getConnectionController().registerReceiver(UserDisconnectedDTO.class, (ProtocolReceiver<UserDisconnectedDTO>) dto -> userDisconnected(dto));
        microServerProxy.getConnectionController().registerReceiver(UserLeftRoomDTO.class, (ProtocolReceiver<UserLeftRoomDTO>) dto -> userLeftRoom(dto));
        microServerProxy.getConnectionController().registerReceiver(UserEnteredRoomDTO.class, (ProtocolReceiver<UserEnteredRoomDTO>) dto -> userEnteredRoom(dto));
        microServerProxy.getConnectionController().registerReceiver(SetCurrentUserDTO.class, (ProtocolReceiver<SetCurrentUserDTO>) dto -> setCurrentUser(dto));
        microServerProxy.getConnectionController().registerReceiver(MessageInRoomDTO.class, (ProtocolReceiver<MessageInRoomDTO>) dto -> messageInRoom(dto));
        microServerProxy.getConnectionController().registerReceiver(UserVerbsDTO.class, (ProtocolReceiver<UserVerbsDTO>) dto -> userVerbsReceived(dto));
    }

    private void userVerbsReceived(UserVerbsDTO dto) {
        //TODO
    }

    private void messageInRoom(MessageInRoomDTO dto) {
        mainActivity.addText(dto.getMessage());
    }



    private void userLeftRoom(UserLeftRoomDTO dto) {
        mainActivity.addText(dto.getUserName() + " left the room");
    }

    private void userDisconnected(UserDisconnectedDTO dto) {
        mainActivity.addText(dto.getUserName() + " disconnected");
    }


    private void userInRoom(UserInRoomDTO dto) {
        mainActivity.addText(dto.getUser().getName() + " is here");
     }

    private void setCurrentUser(SetCurrentUserDTO dto) {
        currentUser = dto.getUser();
    }


    private void userEnteredRoom(UserEnteredRoomDTO dto) {
        mainActivity.addText(dto.getUser().getName() + " entered (from " + dto.getOldRoomName() +")");
     }



    private void setRoom(RoomDto room) {
        currentRoom = room;
        mainActivity.addText("You are now in " + room.getName() );
    }


    @Override
    public void connected() {
        mainActivity.addText("Connected");
    }

    @Override
    public void disconnected() {
        mainActivity.addText("Disconnected");
    }

}
