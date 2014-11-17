package com.mfurseman.robo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class RoboClient implements Runnable {

    private final String address;
    private final int port = 5678;
    private final SocketConnectionAdapter socketConnectionAdapter;

    RoboClient(String address, SocketConnectionAdapter socketConnectionAdapter) {
        this.address = address;
        this.socketConnectionAdapter = socketConnectionAdapter;
    }

    @Override
    public void run() {
        Socket socket = null;
        DataOutputStream outToServer = null;
        BufferedReader inFromServer = null;

        try {
            socket = new Socket(address, port);
            outToServer = new DataOutputStream(socket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketConnectionAdapter.getHandler().post(socketConnectionAdapter.getOnConnection());

            Thread reader = new Thread(new SocketReader(inFromServer, socketConnectionAdapter));
            reader.start();

            while(socket.isConnected()) {
                // Only check at refresh rate
                Thread.sleep(1000 / 60, 0);
                synchronized (socketConnectionAdapter.getCommandList()) {
                    while (!socketConnectionAdapter.getCommandList().isEmpty()) {
                        String command = socketConnectionAdapter.getCommandList().remove(0);
                        Log.d(ControlActivity.TAG, "Sending to server: " + command);
                        outToServer.writeBytes(command);
                    }
                }
            }
            reader.interrupt();
        } catch (IOException e) {
            Log.w(ControlActivity.TAG, "No connection");
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.w(ControlActivity.TAG, "Disconnected");
            e.printStackTrace();
        }
    }

    class SocketReader implements Runnable {

        private final BufferedReader reader;
        private final SocketConnectionAdapter socketConnectionAdapter;

        SocketReader(BufferedReader reader, SocketConnectionAdapter socketConnectionAdapter) {
            this.reader = reader;
            this.socketConnectionAdapter = socketConnectionAdapter;
        }

        @Override
        public void run() {
            try {
                while(true) {
                    char[] buffer = new char[5];
                    int response = reader.read(buffer);
                    Log.d(ControlActivity.TAG,
                            "Read from server: " + response + " : " + String.valueOf(buffer));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
