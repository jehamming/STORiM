package nl.hamming.storimapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.common.net.ProtocolReceiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import nl.hamming.storimapp.R;
import nl.hamming.storimapp.STORIMClientApplication;
import nl.hamming.storimapp.STORIMConnectionDetails;
import nl.hamming.storimapp.controllers.LoginController;

public class LoginActivity extends AppCompatActivity {



    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        MicroServerProxy microServerProxy =  STORIMClientApplication.getInstance().getStorimClientController().getMicroServerProxy();

        loginController = new LoginController(this, microServerProxy);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginController.storeRecents();;
    }
}