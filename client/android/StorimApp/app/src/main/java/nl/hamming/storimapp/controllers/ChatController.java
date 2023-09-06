package nl.hamming.storimapp.controllers;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.dto.RoomDto;
import com.hamming.storim.common.dto.UserDto;
import com.hamming.storim.common.dto.protocol.request.ExecVerbDTO;
import com.hamming.storim.common.dto.protocol.serverpush.MessageInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetCurrentUserDTO;
import com.hamming.storim.common.dto.protocol.serverpush.SetRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserDisconnectedDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserEnteredRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserInRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserLeftRoomDTO;
import com.hamming.storim.common.dto.protocol.serverpush.UserVerbsDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import java.util.ArrayList;
import java.util.List;

import nl.hamming.storimapp.R;
import nl.hamming.storimapp.adapters.VerbSpinItem;
import nl.hamming.storimapp.ui.MainActivity;

public class ChatController implements ConnectionListener {


    private UserDto currentUser;

    private RoomDto currentRoom;

    private MicroServerProxy microServerProxy;
    private MainActivity mainActivity;
    private Spinner verbsSpinner;
    private List<VerbSpinItem> verbItems = new ArrayList<>();
    private ArrayAdapter<VerbSpinItem> verbSpinAdapter;
    private EditText txtInput;
    private TextView textView;
    private ScrollView scrollView;
    private TextView txtRoomName;

    public ChatController(MainActivity mainActivity, MicroServerProxy microServerProxy) {
        this.microServerProxy = microServerProxy;
        this.mainActivity = mainActivity;
        microServerProxy.getConnectionController().addConnectionListener(this);
        registerReceivers();
        setup();
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

    private void setup() {
        verbsSpinner = mainActivity.findViewById(R.id.verbsSpinner);
        txtInput = mainActivity.findViewById(R.id.txtInput);
        textView = mainActivity.findViewById(R.id.txtViewHistory);
        scrollView = mainActivity.findViewById(R.id.scrollableView);
        txtRoomName = mainActivity.findViewById(R.id.txtRoomName);

        verbSpinAdapter=  new ArrayAdapter(mainActivity,
                android.R.layout.simple_spinner_dropdown_item,  verbItems);
        verbsSpinner.setAdapter(verbSpinAdapter);

        Button button = (Button) mainActivity.findViewById(R.id.btnSend);
        button.setOnClickListener(v -> sendText());
    }


    private void userVerbsReceived(UserVerbsDTO dto) {
        mainActivity.runOnUiThread(() -> {
            for (Long verbId: dto.getVerbs().keySet() ) {
                VerbSpinItem item = new VerbSpinItem(verbId, dto.getVerbs().get(verbId));
                verbSpinAdapter.add(item);
            }
        });

    }

    private void messageInRoom(MessageInRoomDTO dto) {
        addText(dto.getMessage());
    }



    private void userLeftRoom(UserLeftRoomDTO dto) {
        addText(dto.getUserName() + " left the room");
    }

    private void userDisconnected(UserDisconnectedDTO dto) {
        addText(dto.getUserName() + " disconnected");
    }


    private void userInRoom(UserInRoomDTO dto) {
        addText(dto.getUser().getName() + " is here");
     }

    private void setCurrentUser(SetCurrentUserDTO dto) {
        currentUser = dto.getUser();
    }


    private void userEnteredRoom(UserEnteredRoomDTO dto) {
        addText(dto.getUser().getName() + " entered (from " + dto.getOldRoomName() +")");
     }



    private void setRoom(RoomDto room) {
        currentRoom = room;
        if (room != null) {
            String text = room.getRoomURI() + " (" + room.getName() + ")";
            mainActivity.runOnUiThread(() ->txtRoomName.setText(text));
            addText("You are now in " + room.getName() );
        }
    }


    private void sendText() {
        String txt = txtInput.getText().toString().trim();
        Long id = getCurrentVerb();
        if (! "".equals(txt) && id != null ) {
            microServerProxy.executeVerb(id, txt);
            mainActivity.runOnUiThread(() -> txtInput.setText(""));
        }
    }

    private Long getCurrentVerb() {
        Long id = null;
        VerbSpinItem item = (VerbSpinItem) verbsSpinner.getSelectedItem();
        if (item != null ) {
            id = item.getVerbId();
        }
        return id;
    }

    @Override
    public void connected() {
        addText("Connected");
    }

    @Override
    public void disconnected() {
        addText("Disconnected");
    }

    public void addText(String txt) {
        mainActivity.runOnUiThread(() -> {
            String text = String.valueOf(textView.getText());
            text = text.concat(txt + " \n");
            textView.setText(text);
            scrollView.fullScroll(View.FOCUS_DOWN);
        });
    }

}
