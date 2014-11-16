package com.mfurseman.robo;

import android.os.Handler;

import java.util.List;


public interface SocketConnectionAdapter {
    public Handler getHandler();
    public Runnable getOnConnection();
    public Runnable getOnCommandReceived();
    public List<String> getCommandList();
}
