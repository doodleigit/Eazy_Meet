package net.doodlei.android.eazymeet.utils;

import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketIOManager {

    private static final String TAG = SocketIOManager.class.getSimpleName();

    private static SocketIOManager socketIOManager = null;
    public Socket socket = null;

    public static SocketIOManager newInstance() {
        if (socketIOManager == null) {
            socketIOManager = new SocketIOManager();
        }
        return socketIOManager;
    }

    private void initializeSocket() {
        try {
            socket = IO.socket(AppConstants.BASE_URL_SOCKET);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "EVENT connect");
                    String id = socket.connect().id();
                    Log.d(TAG, "call: " + id);
                }
            }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if (args != null) {
                        Log.e(TAG, "Event error: " + args[0].toString());
                    }
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e("TAG", "Event disconnect, Socket is disconnected");
                }
            }).on("test", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if (args != null) {
                        Log.e(TAG, "Event error: " + args[0].toString());
                    }
                }
            });

            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocketInstance() {
        if (socket == null) {
            initializeSocket();
        }
        return socket;
    }

}
