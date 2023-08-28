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

import nl.hamming.storimapp.R;
import nl.hamming.storimapp.STORIMClientApplication;

public class LoginActivity extends AppCompatActivity {

    public static String LOGIN_SUCCESS = "LoginSuccess" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        MicroServerProxy microServerProxy =  STORIMClientApplication.getInstance().getStorimClientController().getMicroServerProxy();

        microServerProxy.getConnectionController().registerReceiver(LoginResultDTO.class, (ProtocolReceiver<LoginResultDTO>) dto -> loginResult(dto));

        final Button button = (Button) findViewById(R.id.btnLogin);
        button.setOnClickListener(v -> login());
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

    public void loginResult(LoginResultDTO dto) {
        if (dto.isSuccess()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(LOGIN_SUCCESS, true);
            startActivity(intent);
        } else {
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), dto.getErrorMessage(), Toast.LENGTH_LONG).show());
        }
    }


}