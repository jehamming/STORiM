package nl.hamming.storimapp.ui;

import android.content.Context;
import android.widget.Toast;

import com.hamming.storim.common.MicroServerProxy;
import com.hamming.storim.common.StorimURI;
import com.hamming.storim.common.dto.protocol.requestresponse.LoginResultDTO;

import nl.hamming.storimapp.STORIMClientApplication;

public class LoginTask implements Runnable{

    private Context context;
    private String storimURI, username, password;

    public LoginTask(Context context, String storimURI, String username, String password) {
        this.storimURI = storimURI;
        this.username = username;
        this.password = password;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            MicroServerProxy microServerProxy =  STORIMClientApplication.getInstance().getStorimClientController().getMicroServerProxy();
            StorimURI uri = new StorimURI(storimURI);
            // Connect to server
            microServerProxy.connect(uri);
            // Do login request
            microServerProxy.login(username, password, uri.getRoomId());
        } catch (Exception e) {
           System.err.println("Error:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
