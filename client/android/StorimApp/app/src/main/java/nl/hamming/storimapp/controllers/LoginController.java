package nl.hamming.storimapp.controllers;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;
import com.hamming.storim.common.interfaces.ConnectionListener;
import com.hamming.storim.common.net.ProtocolReceiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import nl.hamming.storimapp.R;
import nl.hamming.storimapp.STORIMConnectionDetails;
import nl.hamming.storimapp.ui.LoginActivity;
import nl.hamming.storimapp.ui.LoginTask;

public class LoginController implements ConnectionListener {


    private LoginActivity loginActivity;
    private static String RECENTS_FILENAME = "recents.dat";
    private List<STORIMConnectionDetails> recents;
    private Spinner recentsSpinner;
    private ArrayAdapter<STORIMConnectionDetails> recentsSpinAdapter;
    private MicroServerProxy microServerProxy;
    private EditText txtIp;
    EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;

    public LoginController(LoginActivity loginActivity, MicroServerProxy microServerProxy) {
        this.microServerProxy = microServerProxy;
        this.loginActivity = loginActivity;
        microServerProxy.getConnectionController().addConnectionListener(this);
        registerReceivers();
        setup();
    }

    private void registerReceivers() {
        microServerProxy.getConnectionController().registerReceiver(LoginResultDTO.class, (ProtocolReceiver<LoginResultDTO>) dto -> loginResult(dto));
    }

    private void setup() {
        txtIp = loginActivity.findViewById(R.id.txtIP);
        txtUsername = loginActivity.findViewById(R.id.txtUsername);
        txtPassword = loginActivity.findViewById(R.id.txtPassword);
        btnLogin = loginActivity.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> login());
        //Recents
        recents = new ArrayList<>();
        recentsSpinner = loginActivity.findViewById(R.id.recentsSpinner);
        recentsSpinAdapter = new ArrayAdapter(loginActivity,
                android.R.layout.simple_spinner_dropdown_item, recents);
        recentsSpinner.setAdapter(recentsSpinAdapter);
        recentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                recentClicked(recents.get(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        loadRecents();
    }

    private void recentClicked(STORIMConnectionDetails d) {
        if ( d != null ) {
            loginActivity.runOnUiThread(() -> {
                txtIp.setText(d.getConnectURL());
                txtUsername.setText(d.getUsername());
                txtPassword.setText(d.getPassword());
            });
        }
    }


    private void login() {
        String strIP = txtIp.getText().toString().trim();
        String strUsername = txtUsername.getText().toString().trim();
        String strPassword = txtPassword.getText().toString().trim();

        LoginTask task = new LoginTask(loginActivity.getApplicationContext(), strIP, strUsername, strPassword);
        Thread t = new Thread(task);
        t.start();
    }

    public void loginResult(LoginResultDTO dto) {
        if (dto.isSuccess()) {
            loginActivity.finish();
            storeConnectionDetails();
        } else {
            loginActivity.runOnUiThread(() -> Toast.makeText(loginActivity.getApplicationContext(), dto.getErrorMessage(), Toast.LENGTH_LONG).show());
        }
    }

    private void storeConnectionDetails() {
        String url = txtIp.getText().toString().trim();
        String username = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();


        loginActivity.runOnUiThread(() -> {
            STORIMConnectionDetails details = findRecent(url + "-" + username);
            if (details == null) {
                details = new STORIMConnectionDetails(url, username, password);
                recentsSpinAdapter.add(details);
            } else {
                details.setPassword(password);
            }
        });


    }

    private STORIMConnectionDetails findRecent(String name) {
        STORIMConnectionDetails found = null;
        for (STORIMConnectionDetails d : recents) {
            if (d.toString().equals(name)) {
                found = d;
                break;
            }
        }
        return found;
    }

    public void storeRecents() {
        File cacheDir = loginActivity.getCacheDir();
        File file = new File(cacheDir.getAbsolutePath() + "/" + RECENTS_FILENAME);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(recents);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRecents() {
        File cacheDir = loginActivity.getCacheDir();
        File file = new File(cacheDir.getAbsolutePath() + "/" + RECENTS_FILENAME);
        try {
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                recents = (List<STORIMConnectionDetails>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        loginActivity.runOnUiThread(() -> {
            for (STORIMConnectionDetails d : recents) {
                recentsSpinAdapter.add(d);
            }
        });
    }

    @Override
    public void connected() {

    }

    @Override
    public void disconnected() {
    }
}
