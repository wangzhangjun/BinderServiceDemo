package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MessengerService extends Service {
    Handler messengerHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case  1:
                    Log.i("test","MessengerService Messenger handleMessage msg = " + msg + " bundle  key value = " + msg.getData().getString("bundleKey"));
                    Messenger clientSend = msg.replyTo;
                    Message toClientMsg = Message.obtain();
                    toClientMsg.what = 2;
                   // toClientMsg.obj = "I am replay from Server";
                    try {
                        clientSend.send(toClientMsg);
                    }catch (Exception e) {
                        Log.i("test","MessengerService clientSend  error ",e);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    Messenger messenger = new Messenger(messengerHandler);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

}
