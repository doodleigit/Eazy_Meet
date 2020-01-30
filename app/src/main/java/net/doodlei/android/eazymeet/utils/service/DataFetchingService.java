package net.doodlei.android.eazymeet.utils.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import net.doodlei.android.eazymeet.utils.SocketIOManager;

import io.socket.client.Socket;

public class DataFetchingService extends Service {

    private Socket socket;

    @Override
    public void onCreate() {
        super.onCreate();

        if (socket != null) {
            if (!socket.connected()) {
                socket = SocketIOManager.newInstance().socket;
//                Toast.makeText(context, "Web Socket Reconnected", Toast.LENGTH_LONG).show();
            }
        } else {
            socket = SocketIOManager.newInstance().getSocketInstance();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!socket.connected()) {
            socket = new SocketIOManager().getSocketInstance();
//                Toast.makeText(context, "Web Socket Reconnected", Toast.LENGTH_LONG).show();
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
