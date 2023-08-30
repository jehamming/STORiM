package nl.hamming.storimapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hamming.storim.common.MicroServerProxy;

import nl.hamming.storimapp.R;
import nl.hamming.storimapp.STORIMClientApplication;
import nl.hamming.storimapp.controllers.ChatController;
import nl.hamming.storimapp.controllers.GameViewController;
import nl.hamming.storimapp.view.GameView;

public class MainActivity extends AppCompatActivity {
    private DisplayMetrics mMetrics = new DisplayMetrics();
    private float mScreenDensity;
    private ChatController chatController;
    private GameViewController gameViewController;
    private MicroServerProxy microServerProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        mScreenDensity = mMetrics.density;
        setContentView(R.layout.activity_main);

        microServerProxy = STORIMClientApplication.getInstance().getStorimClientController().getMicroServerProxy();
        chatController = new ChatController(this, microServerProxy);

        GameView roomView = findViewById(R.id.roomView);
        gameViewController = new GameViewController(roomView, microServerProxy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogin:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuDisconnect:
                if ( microServerProxy.isConnected()) {
                    microServerProxy.disconnect();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}