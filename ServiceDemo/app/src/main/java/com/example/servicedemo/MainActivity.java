package com.example.servicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    Handler messengerHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 2) {
                Log.i("test","messengerHandler handleMessage msg.what == 2 msg = " + msg);
            }
            super.handleMessage(msg);
        }
    };
    Messenger messengerClientSend = new Messenger(messengerHandler);
    Messenger messengerServer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("zhjwang","first test");  //右小角的按钮
                Intent intent = new Intent(MainActivity.this,MyService.class);
                bindService(intent, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        IStudentInterface remoteInterface = IStudentInterface.Stub.asInterface(service);
                        try {
                            service.linkToDeath(new IBinder.DeathRecipient() {
                                @Override
                                public void binderDied() {
                                    Log.i("zhjwang"," MyService Died");
                                }
                            },0);
                            remoteInterface.setCallback(new CallbackeService());
                            Log.i("test","client onServiceConnected remoteInterface getStudentId = " + remoteInterface.getStudentId("helloworld"));
                            Log.i("test","client onServiceConnected remoteInterface getStudentId = " + remoteInterface.getStudentId("no"));
                            remoteInterface.getStudentId("dead");  //发送消息，让service dead
                            /***************************************************************/
                            StudentInfo studentInfoIn = new StudentInfo();
                            studentInfoIn.setId("200");
                            studentInfoIn.setName("client");
                            Log.i("test","client onServiceConnected remoteInterface getConvertName = " +remoteInterface.getConvertName(studentInfoIn ) + " studentInfoIn = " + studentInfoIn);
                            /***************************************************************/
                            StudentInfo studentInfoOut = new StudentInfo();
                            studentInfoOut.setId("200+");
                            studentInfoOut.setName("clientOut");
                            remoteInterface.getServiceStudentInfo(studentInfoOut);
                            Log.i("test","client onServiceConnected remoteInterface getServiceStudentInfo = " + studentInfoOut);

                            /***************************************************************/
                            StudentInfo studentInfoInOut = new StudentInfo();
                            studentInfoInOut.setId("2001");
                            studentInfoInOut.setName("clientInOut");
                            remoteInterface.getServiceStudentInfoInOut(studentInfoInOut);
                            Log.i("test","client onServiceConnected remoteInterface getServiceStudentInfoInOut = " + studentInfoInOut);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        Log.i("test","client onServiceDisconnected name = " +name);
                    }
                }, BIND_AUTO_CREATE);
            }
        });

        FloatingActionButton fab1 = findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("zhjwang","second test");  //左下角按钮
                Intent intent = new Intent(MainActivity.this,MessengerService.class);
                Log.i("test","MessengerService  onClick ");
                bindService(intent, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        try {
                            Log.i("test","MessengerService  onServiceDisconnected name = " +name);
                            messengerServer = new Messenger(service);
                            sendMessageToServer();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("test","error " ,e);
                        }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        Log.i("test","client onServiceDisconnected name = " +name);
                    }
                }, BIND_AUTO_CREATE);
            }
        });
    }
    void sendMessageToServer() throws RemoteException {
        Message toServer = Message.obtain();
        toServer.replyTo = messengerClientSend;
        toServer.what = 1;
        Bundle bundle = new Bundle();
        bundle.putString("bundleKey","bundleValue Client");
        toServer.setData(bundle);
        messengerServer.send(toServer);
    }

    class CallbackeService extends IChangeCallback.Stub {
        @Override
        public int changeData(int changeIndex) throws RemoteException {
            return  changeIndex + 1;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}