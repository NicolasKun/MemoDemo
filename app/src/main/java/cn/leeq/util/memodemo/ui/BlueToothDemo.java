package cn.leeq.util.memodemo.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.btsdk.BluetoothService;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.utils.PrintUtil;

public class BlueToothDemo extends AppCompatActivity {
    private BluetoothAdapter adapter;
    BluetoothDevice device = null;
    BluetoothService mService = null;
    private TextView tvDeviceName;
    private RadioButton rbSearch;
    private RadioButton rbPrintText;
    private RadioButton rbPrintTicket;
    private ImageView iv;
    private PrintUtil printUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth_demo);
        setTitle("CPCL指令打印机演示");
        init();
    }

    private void init() {
        rbSearch = (RadioButton) findViewById(R.id.bt_rb_search_blue_tooth);
        iv = (ImageView) findViewById(R.id.bt_iv);
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

        printUtil = new PrintUtil(mService);
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
                StringBuffer s = new StringBuffer();
                s.append(printUtil.drawControll(300));
                s.append(printUtil.drawText(0, 0, 10, 10, "北京市丰台区纪家庙"));
                s.append(printUtil.drawBarCode(1, 1, 70, 150, 10, "623100000056"));
                s.append(printUtil.drawLine(10, 150, 10, 300));
                s.append(printUtil.drawPrint());
                String ss = s.toString();
                mService.sendMessage(ss,"GBK");
                break;
            case R.id.bt_rb_print_ticket:
                StringBuffer sb = new StringBuffer();
                sb.append(printUtil.drawControll(400));
                sb.append(printRecStub());
                sb.append(printUtil.drawPrint());
                Log.e("test", "打印\n" + sb.toString());
                mService.sendMessage(sb.toString(),"GBK");
                break;
        }
    }


    private String printRecStub() {
        StringBuffer sb = new StringBuffer();
        sb.append(printUtil.drawBox(10, 10, 570, 340));
        sb.append(printUtil.drawBox(10, 10, 500, 70));
        sb.append(printUtil.drawBox(10, 70, 330, 180));
        sb.append(printUtil.drawBox(10, 180, 330, 280));
        sb.append(printUtil.drawLine(500, 10, 500, 340));
        sb.append(printUtil.drawLine(330, 280, 330, 340));
        sb.append(printUtil.drawLine(330, 230, 500, 230));
        sb.append(printUtil.drawText(0, 1, 13, 73, "寄件方:"));
        sb.append(printUtil.drawText(0, 1, 18, 108, "小瑞   13606237330"));
        sb.append(printUtil.drawText(0, 1, 18, 143, "丰台区纪家庙天瑞大厦401"));
        sb.append(printUtil.drawText(0, 1, 13, 183, "收件方:"));
        sb.append(printUtil.drawText(0, 1, 18, 218, "宝宝   18330226823"));
        sb.append(printUtil.drawText(0, 1, 18, 253, "朝阳区朝外SOHO A座1308"));
        sb.append(printUtil.drawBarCode(1, 1, 50, 170, 15, "8100101"));
        sb.append(printUtil.drawVText(0, 2, 530, 170, "收件人存根联"));
        return sb.toString();
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
