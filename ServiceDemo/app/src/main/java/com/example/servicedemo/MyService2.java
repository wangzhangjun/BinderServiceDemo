package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MyService2 extends Service {
    Handler handler = new Handler();
    public MyService2() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
    IStudentInterface2.Stub mBinder = new IStudentInterface2.Stub() {
        @Override
        public int getStudentId2(String name) throws RemoteException {
            if(name.equals("dead2")){
                handler.postDelayed(new Runnable() {  //这里要放到一个Runnable里面，要不然这个错误会返回到客户端，导致客户端crash
                    @Override
                    public void run() {
                        String str = null;
                        str.length();
                    }
                },100);
            }
            return 123;
        }
    };
}
