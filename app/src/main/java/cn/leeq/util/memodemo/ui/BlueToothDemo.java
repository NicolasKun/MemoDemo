package cn.leeq.util.memodemo.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.btsdk.BluetoothService;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

import cn.leeq.util.memodemo.R;

public class BlueToothDemo extends AppCompatActivity {
    private BluetoothAdapter adapter;
    BluetoothDevice device = null;
    BluetoothService mService = null;
    private TextView tvDeviceName;
    private RadioButton rbSearch;
    private RadioButton rbPrintText;
    private RadioButton rbPrintTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth_demo);
        init();
    }

    private void init() {
        rbSearch = (RadioButton) findViewById(R.id.bt_rb_search_blue_tooth);
        tvDeviceName = (TextView) findViewById(R.id.bt_tv_device_name);
        adapter = BluetoothAdapter.getDefaultAdapter();
        rbPrintText = (RadioButton) findViewById(R.id.bt_rb_print_text);
        rbPrintTicket = (RadioButton) findViewById(R.id.bt_rb_print_ticket);
        mService = new BluetoothService(this, handler);
        if (!mService.isAvailable()) {  //蓝牙不可用
            Toast.makeText(BlueToothDemo.this, "蓝牙不可用", Toast.LENGTH_SHORT).show();
            finish();
        }
        rbPrintText.setEnabled(false);
        rbPrintTicket.setEnabled(false);
    }

    private void openBlueTooth() {
        //打开蓝牙
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
        }
    }

    public void blueTooth(View view) {
        switch (view.getId()) {
            case R.id.bt_rb_search_blue_tooth:
                if (adapter == null) {
                    Toast.makeText(BlueToothDemo.this, "该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
                } else {
                    if (adapter.isEnabled()) {
                        startActivityForResult(new Intent(this, SelectDevice.class), 100);
                    } else {
                        Toast.makeText(BlueToothDemo.this, "蓝牙尚未开启", Toast.LENGTH_SHORT).show();
                        openBlueTooth();
                    }
                }
                break;
            case R.id.bt_rb_print_text:
                String text = "诛仙手游8月10日11时公测\n\n";
                if (text.length() > 0) {
                    mService.sendMessage(text, "GBK");
                }
                break;
            case R.id.bt_rb_print_ticket:
                printTicket();
                break;
        }
    }

    /**
     * 打印小票
     */
    private void printTicket() {
        byte[] cmd = new byte[3];
        cmd[0] = 0x1b;
        cmd[1] = 0x21;

        cmd[2] |= 0x10;   //倍高 倍宽模式
        mService.write(cmd);
        mService.sendMessage("快递宝\n","GBK");

        cmd[2] &= 0xEF;
        mService.write(cmd);
        String msg = "运单号 :" + 8100012 +
                "\n收件人 : " + "老唐" +
                "\n收件人电话 : " + "18163215156" +
                "\n收件地址 : " + "北京市丰台区天瑞大厦401\n" +
                "\n网点 : " + "北京市丰台区纪家庙申通快递KKK部\n\n";
        mService.sendMessage(msg, "GBK");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == 101) {
                String addr = data.getStringExtra(SelectDevice.EXTRA_DEVICE_ADDRESS);
                Log.e("test", "回调地址 " + addr);
                tvDeviceName.setText(addr);
                device = mService.getDevByMac(addr);

                mService.connect(device);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(BlueToothDemo.this, "连接成功", Toast.LENGTH_SHORT).show();
                            rbPrintText.setEnabled(true);
                            rbPrintTicket.setEnabled(true);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.e("test", "蓝牙正在连接.... " + msg.what);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.e("test", "蓝牙正在连接.... " + msg.what);
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    Toast.makeText(BlueToothDemo.this, "设备连接中断", Toast.LENGTH_SHORT).show();
                    rbPrintText.setEnabled(false);
                    rbPrintTicket.setEnabled(false);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    Toast.makeText(BlueToothDemo.this, "无法连接到设备", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            mService.stop();
        }
        mService = null;
    }
}
