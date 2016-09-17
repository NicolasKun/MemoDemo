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
                sb.append(printUtil.drawControll(880));
                sb.append(printSendStub());
                sb.append(printUtil.drawPrint());
                sb.append(printUtil.drawControll(370));
                sb.append(printRecStub());
                sb.append(printUtil.drawPrint());
                sb.append(printUtil.drawControll(550));
                sb.append(printSenderStub());
                sb.append(printUtil.drawPrint());
                Log.e("test", "打印\n" + sb.toString());
                mService.sendMessage(sb.toString(),"GBK");
                break;
        }
    }

    /**
     * 第一联 865
     */
    private String printSendStub() {
        StringBuffer sb = new StringBuffer();
        sb.append(printUtil.drawBox(0, 0, 570, 865));    //最外框
        sb.append(printUtil.drawLine(500, 0, 500, 865)); //右侧说明框
        sb.append(printUtil.drawLine(0, 87, 500, 87));   //最上
        sb.append(printUtil.drawLine(0, 219, 500, 219));  //最上-1  条码
        sb.append(printUtil.drawLine(0, 460, 500, 460));  //最上-2  收寄人信息
        sb.append(printUtil.drawLine(0, 340, 340, 340));
        sb.append(printUtil.drawLine(340, 219, 340, 460));
        sb.append(printUtil.drawLine(0, 559, 500, 559));   //订单详情
        sb.append(printUtil.drawLine(0, 608, 500, 608));   //签名
        sb.append(printUtil.drawLine(250, 559, 250, 608));
        sb.append(printUtil.drawBarCode(1, 1, 70, 120, 100, "623100000059"));
        sb.append(printUtil.drawText(0, 1, 130, 175, "623100000059"));
        sb.append(printUtil.drawText(0, 1, 3, 225, "寄件方:"));
        sb.append(printUtil.drawText(0, 1, 8, 260, " 小蕊   17123145342"));
        sb.append(printUtil.drawText(0, 1, 8, 295, " 北京市丰台区天瑞大厦701"));
        sb.append(printUtil.drawText(0, 1, 3, 345, "收件方:"));
        sb.append(printUtil.drawText(0, 1, 3, 380, " 宝宝   18330226823"));
        sb.append(printUtil.drawText(0, 1, 3, 415, " 朝阳区朝外SOHO A座1309"));
        sb.append(printUtil.drawText(0, 1, 343, 225, "目的地"));

        sb.append(printUtil.drawText(0 , 1, 3  , 570, "收方签名:"));
        sb.append(printUtil.drawText(0 , 1, 253, 570, "收件时间:"));
        sb.append(printUtil.drawVText(0, 2, 530, 700, "派件存根联"));
        return sb.toString();
    }

    /**
     * 第二联355
     */
    private String printRecStub() {
        StringBuffer sb = new StringBuffer();
        sb.append(printUtil.drawBox(0, 0, 570, 355));
        sb.append(printUtil.drawBox(0, 0, 500, 70));
        sb.append(printUtil.drawBox(0, 70, 330, 170));
        sb.append(printUtil.drawBox(0, 170, 330, 270));
        sb.append(printUtil.drawLine(500, 0, 500, 355));
        sb.append(printUtil.drawLine(330, 270, 330, 355));
        sb.append(printUtil.drawLine(330, 214, 500, 214));
        sb.append(printUtil.drawText(0, 1, 3, 73, "寄件方:"));
        sb.append(printUtil.drawText(0, 1, 8, 108, "小瑞   13606237330"));
        sb.append(printUtil.drawText(0, 1, 8, 143, "丰台区纪家庙天瑞大厦401"));
        sb.append(printUtil.drawText(0, 1, 3, 175, "收件方:"));
        sb.append(printUtil.drawText(0, 1, 13, 210, "宝宝   18330226823"));
        sb.append(printUtil.drawText(0, 1, 13, 245, "朝阳区朝外SOHO A座1308"));
        sb.append(printUtil.drawBarCode(1, 1, 50, 170, 10, "8100101"));
        sb.append(printUtil.drawVText(0, 2, 530, 180, "收件人存根联"));
        return sb.toString();
    }

    /**
     * 第三联   85  269
     * 517
     */
    private String printSenderStub() {
        StringBuffer sb = new StringBuffer();
        sb.append(printUtil.drawBox(0, 0, 570, 517));    //最外框
        sb.append(printUtil.drawLine(500, 0, 500, 517)); //右侧说明框
        sb.append(printUtil.drawLine(0, 96, 500, 96));    //条码
        sb.append(printUtil.drawLine(0, 192, 500, 192));  //寄件方信息
        sb.append(printUtil.drawLine(0, 288, 500, 288)); //收件方信息
        sb.append(printUtil.drawLine(0, 368, 500, 368)); //订单详情

        sb.append(printUtil.drawBarCode(1, 1, 60, 120, 8, "623100000121"));
        sb.append(printUtil.drawText(0, 1, 140, 70, "623100000121"));
        sb.append(printUtil.drawText(0, 1, 3, 101, "寄件方:"));
        sb.append(printUtil.drawText(0, 1, 8, 131, " 小蕊    13823515462"));
        sb.append(printUtil.drawText(0, 1, 8, 161, " 丰台区纪家庙天瑞大厦301"));
        sb.append(printUtil.drawText(0, 1, 3, 200, "收件方:"));
        sb.append(printUtil.drawText(0, 1, 8, 230, " 宝宝    18330226823"));
        sb.append(printUtil.drawText(0, 1, 8, 260, " 朝阳区朝外SOHO A座1309"));

        sb.append(printUtil.drawVText(0, 2, 530, 360, "寄件人存根联"));
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
