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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import nl.hamming.storimapp.R;
import nl.hamming.storimapp.view.GameView;

public class MainActivity extends AppCompatActivity {
    private GameView mGameView = null;
    private DisplayMetrics mMetrics = new DisplayMetrics();
    private float mScreenDensity;
    private Boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        mScreenDensity = mMetrics.density;
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.btnSend);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               sendText();
            }
        });

        loggedIn = getIntent().getBooleanExtra(LoginActivity.LOGIN_SUCCESS, false);
        if (loggedIn != null ) {
            addText("You logged into the STORIM Server!");
        }

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addText(String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final TextView textView = (TextView) findViewById(R.id.txtViewHistory);
                ScrollView scrollView = (ScrollView)  findViewById(R.id.scrollView2);
                textView.setText(txt + " \n");
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

    }

    private void sendText() {
        final EditText txtInput = (EditText) findViewById(R.id.txtInput);
        final TextView textView = (TextView) findViewById(R.id.txtViewHistory);
        String txt = txtInput.getText().toString().trim();
        if (! "".equals(txt)) {
            String textViewText = textView.getText().toString();
            textViewText = textViewText.concat("Je zegt '" + txt + "'");
            addText(textViewText);

        }
    }
}