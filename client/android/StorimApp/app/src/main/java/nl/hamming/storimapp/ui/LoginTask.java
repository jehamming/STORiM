package nl.hamming.storimapp.ui;

import android.content.Context;
import android.widget.Toast;

import nl.hamming.storimapp.STORIMClientApplication;

public class LoginTask implements Runnable{

    private Context context;
    private String ip, username, password;

    public LoginTask(Context context, String ip, String username, String password) {
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            STORIMClientApplication.getInstance(context).getControllers().getConnectionController().connect(ip,3333);
            STORIMClientApplication.getInstance(context).getControllers().getUserController().sendLogin(username, password);
        } catch (Exception e) {
           System.err.println("Error:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
