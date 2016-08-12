package cn.leeq.util.memodemo.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

import cn.leeq.util.memodemo.R;

public class BlueToothDemo extends AppCompatActivity {

    private BluetoothAdapter adapter;
    private TextView tvDeviceName;
    private RadioButton rbOpen;
    private RadioButton rbSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth_demo);
        init();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(blueToothBroadCastReceiver, intentFilter);
    }

    private void init() {
        rbOpen = (RadioButton) findViewById(R.id.bt_rb_open_blue_tooth);
        rbSearch = (RadioButton) findViewById(R.id.bt_rb_search_blue_tooth);
        tvDeviceName = (TextView) findViewById(R.id.bt_tv_device_name);
        adapter = BluetoothAdapter.getDefaultAdapter();


    }

    private void openBlueTooth() {
        if (adapter == null) {
            Toast.makeText(BlueToothDemo.this, "该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
        }
        //打开蓝牙
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
        }
    }

    public void blueTooth(View view) {
        switch (view.getId()) {
            case R.id.bt_rb_open_blue_tooth:
                openBlueTooth();
                break;
            case R.id.bt_rb_search_blue_tooth:
                if (adapter.isEnabled()) {
                    adapter.startDiscovery();
                } else {
                    Toast.makeText(BlueToothDemo.this, "蓝牙尚未开启", Toast.LENGTH_SHORT).show();
                    openBlueTooth();
                }
                break;
        }
    }

    BroadcastReceiver blueToothBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String address = device.getAddress();
                    String name = device.getName();
                    tvDeviceName.setText("蓝牙:" + "\n设备名 : " + name + "\n设备地址 : " + address);
                    //如果查找到的设备符合要连接的设备,处理
                    if (name.equals("红米手机")) {
                        //及时关闭
                        adapter.cancelDiscovery();
                        //获取连接状态
                        int bondState = device.getBondState();
                        switch (bondState) {
                            //未配对
                            case BluetoothDevice.BOND_NONE:
                                //配对
                                try {
                                    Method createBond = BluetoothDevice.class.getMethod("createBond");
                                    createBond.invoke(device);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            //已配对
                            case BluetoothDevice.BOND_BONDED:
                                try {
                                    connect(device);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    BluetoothDevice device1 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device1.getName().equalsIgnoreCase("红米手机")) {
                        int bondState = device1.getBondState();
                        switch (bondState) {
                            case BluetoothDevice.BOND_NONE:
                            case BluetoothDevice.BOND_BONDING:
                                break;
                            case BluetoothDevice.BOND_BONDED:
                                try {
                                    connect(device1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(blueToothBroadCastReceiver);
    }

    /**
     * 连接蓝牙设备
     */
    private void connect(BluetoothDevice device) throws IOException {
        String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
        UUID uuid = UUID.fromString(SPP_UUID);
        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
        socket.connect();
    }
}
