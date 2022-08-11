package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.os.Handler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;

public class MyService extends Service {
    Handler handler = new Handler();
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
            //testService2
            Intent intent2 = new Intent(MyService.this, MyService2.class);
            bindService(intent2, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    Log.i("zhjwang", "service2");
                    IStudentInterface2 remoteInterface2 = IStudentInterface2.Stub.asInterface(service);
                    try {
                        service.linkToDeath(new IBinder.DeathRecipient() {
                            @Override
                            public void binderDied() {
                                Log.i("zhjwang"," MyService2 Died");
                                if (mCallBack!= null) {
                                    try {
                                        mCallBack.changeData(123);
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, 0);
                        Log.i("zhjwang","serviceAsClient onServiceConnected remoteInterface getStudentId2 = " + remoteInterface2.getStudentId2("helloworld"));
                        remoteInterface2.getStudentId2("dead2"); //触发service2 crash看上面的MyService2 Died会不会执行
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            }, BIND_AUTO_CREATE);
            //testService2
            if (name.equals( "helloworld")) {
                if (mCallBack!= null) {
                    Log.i("test"," mCallBack changeData result = " + mCallBack.changeData(1111));
                }
                return 1;
            }
            else if(name.equals("dead")){
                handler.postDelayed(new Runnable() {  //这里要放到一个Runnable里面，要不然这个错误会返回到客户端，导致客户端crash
                    @Override
                    public void run() {
                    String str = null;
                    str.length();
                    }
                },100);
                return -1;
            }
            else {
                return  10;
            }
        }
        @Override
        public void setCallback(IChangeCallback callback) throws RemoteException {
            mCallBack = callback;
            mCallBack.asBinder().linkToDeath(new DeathRecipient() {
                @Override
                public void binderDied() {
                    Log.i("zhjwang","myActivity linkToDeath binderDied");
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
