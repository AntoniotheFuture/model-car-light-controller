package com.nonemin.carlightcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.nonemin.carlightcontroller.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import cc.liyongzhi.bluetoothselector.BluetoothConnectCallback;
import cc.liyongzhi.bluetoothselector.MedBluetooth;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private String TAG = "LUM: ";

    //private TextView buttonConnect, buttonLed;
    private BluetoothAdapter mbluetoothAdapter;
    private String bluetoothDeviceMacAddress = "44:44:1B:0F:0D:B8"; //Bluetooth module physical address
    private BluetoothDevice bluetoothDevice = null; // Connected Bluetooth device
    private BluetoothSocket btSocket = null; // Bluetooth communication socket
    private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB"; // SPP service UUID number
    private final static int RESULT_CODE = 100;

    boolean isConnected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mbluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //get the default bluetooth adapter
        if(mbluetoothAdapter == null){
            Toast.makeText(this,"这个设备没有蓝牙",Toast.LENGTH_LONG).show();
            //finish();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);  //Bluetooth search
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mReceiver, filter);


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
            return true;
        }
        if (id == R.id.action_connect) {
            Log.i(TAG, "check link");
            //Jump directly to the Bluetooth settings interface
            if(!isConnected){
                if (!mbluetoothAdapter.isEnabled()){
                    startActivityForResult(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS), RESULT_CODE);
                } else {

                    //以下为调用本库，输入为Context、BluetoothConnectCallback
                    MedBluetooth.connectBluetooth(this, new BluetoothConnectCallback() {
                        @Override
                        // 连接成功或失败后调用。
                        public void connected(BluetoothSocket socket, BluetoothDevice device, Exception e) {
                            if (e != null) {
                                //连接失败
                                isConnected = false;
                            } else {
                                //输出为获得的socket，可以自行存到全局变量里进行数据输入输出操作。
                                //device为本次连接的设备，可调用 device.getAddress() 获得mac地址。
                                //e 为错误。
                                btSocket = socket;
                                bluetoothDeviceMacAddress = device.getAddress();
                                bluetoothDevice = device;
                                isConnected = true;
                                //connectBtSocket();
                            }
                        }
                        @Override
                        // 连接断开后调用
                        public void disconnected() {
                            // 原理为通过捕获系统的广播而调用的。
                            isConnected = false;
                        }
                    });

                }

            } else {
                disconnect();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void send(int command) {
        if (btSocket == null || !btSocket.isConnected()) {
            Toast.makeText(this, "请先连接蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (btSocket != null) {
                OutputStream os = btSocket.getOutputStream();   //Bluetooth connection output stream
                os.write(command);
            } else {
                Toast.makeText(this, "请先连接蓝牙", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectBtSocket() {
        // Get the handle of the Bluetooth device
        bluetoothDevice = mbluetoothAdapter.getRemoteDevice(bluetoothDeviceMacAddress);
        //Turn off scanning before pairing
        if (mbluetoothAdapter.isDiscovering()) {
            mbluetoothAdapter.cancelDiscovery();
        }
        // Get the connected socket
        try {
            btSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
            //btSocket.connect();  //Connection socket
        } catch (IOException e) {
            Toast.makeText(this, "Connection failed, can't get Socket！" + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        if (btSocket.isConnected()) {
            Log.i(TAG, "socket connected");
            Toast.makeText(this, "connect success", Toast.LENGTH_SHORT).show();
            //buttonConnect.setText("蓝牙已连接");
            isConnected = true;
        } else {
            Log.i(TAG, "socket didn't connected");
            Toast.makeText(this, "connect failed", Toast.LENGTH_SHORT).show();
            isConnected = false;
        }
    }

    private void disconnect() {
        try {
            if (btSocket != null) {
                btSocket.close();
            }
        } catch (IOException e) {
        }
        //buttonConnect.setText("连接蓝牙");
        isConnected = false;
    }




    // Help us find the physical address of the Bluetooth module that needs to be connected
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i(TAG, "Searched Bluetooth device;  device name: " + device.getName() + "  device address: " + device.getAddress());
            }
            if(btSocket == null){
                return;
            }
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                Log.i(TAG, "ACTION_ACL_CONNECTED");
                if (btSocket.isConnected()) {
                    //buttonConnect.setText("蓝牙已连接");
                    isConnected = true;
                }
            }
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Log.i(TAG, "ACTION_ACL_CONNECTED");
                if (btSocket.isConnected()) {
                    //buttonConnect.setText("连接蓝牙");
                    isConnected = false;
                }
            }
        }
    };



}