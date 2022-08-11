package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
    IChangeCallback mCallBack = null;
    IStudentInterface.Stub mBinder = new IStudentInterface.Stub() {

        @Override
        public int getStudentId(String name) throws RemoteException {
            if (name.equals( "helloworld")) {
                if (mCallBack!= null) {
                    Log.i("test"," mCallBack changeData result = " + mCallBack.changeData(1111));
                }
                return 1;
            } else {
                return  10;
            }
        }

        @Override
        public void setCallback(IChangeCallback callback) throws RemoteException {
            mCallBack = callback;
            mCallBack.asBinder().linkToDeath(new DeathRecipient() {
                @Override
                public void binderDied() {
                    Log.i("test","linkToDeath binderDied");
                }
            },0);
        }

        @Override
        public String getConvertName(StudentInfo info) throws RemoteException {
            if (info!=null) {
                info.name = "hello,this is ConvertName ";
                return info.name;
            }
            return null;
        }

        @Override
        public void getServiceStudentInfo(StudentInfo serviceInfo) throws RemoteException {
            Log.i("test"," getServiceStudentInfo out serviceInfo = " + serviceInfo);
            serviceInfo.id = "100";
            serviceInfo.name = "this is Service modify out";
        }

        @Override
        public void getServiceStudentInfoInOut(StudentInfo serviceInfo) throws RemoteException {
            Log.i("test"," getServiceStudentInfo inout serviceInfo = " + serviceInfo);
            serviceInfo.id = "-100";
            serviceInfo.name = "this is Service modify in out";
        }
    };
    @Override
    public void onCreate() {
        Log.i("test","MyService onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("test","MyService onStartCommand intent = " + intent );
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i("test","MyService onDestroy");
        super.onDestroy();
    }
}
