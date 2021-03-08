package nl.hamming.storimapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hamming.storim.model.dto.UserDto;

import nl.hamming.storimapp.R;
import nl.hamming.storimapp.STORIMClientApplication;
import nl.hamming.storimapp.interfaces.ConnectionListener;
import nl.hamming.storimapp.interfaces.UserListener;
import nl.hamming.storimapp.view.GameView;

public class LoginActivity extends AppCompatActivity implements UserListener {

    public static String LOGIN_SUCCESS = "LoginSuccess" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        STORIMClientApplication.getInstance(getApplicationContext()).getControllers().getUserController().addUserListener(this);

        final Button button = (Button) findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
             case R.id.menuMain:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void login() {
        final EditText txtIp = (EditText) findViewById(R.id.txtIP);
        final EditText txtUsername = (EditText) findViewById(R.id.txtUsername);
        final EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
        String strIP = txtIp.getText().toString().trim();
        String strUsername = txtUsername.getText().toString().trim();
        String strPassword = txtPassword.getText().toString().trim();

        LoginTask task = new LoginTask(getApplicationContext(), strIP, strUsername, strPassword);
        Thread t = new Thread(task);
        t.start();
    }

    @Override
    public void loginResult(boolean success, String message) {
        if (success) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(LOGIN_SUCCESS, true);
            startActivity(intent);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    public void userConnected(UserDto user) {
        //TODO not used
    }

    @Override
    public void userDisconnected(UserDto user) {
        //TODO Not used
    }


}