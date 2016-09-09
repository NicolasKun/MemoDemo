package cn.leeq.util.memodemo.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.zj.btsdk.BluetoothService;

import java.util.Hashtable;

import HPRTAndroidSDK.HPRTPrinterHelper;
import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.utils.PublicAction;

public class BlueToothDemo extends AppCompatActivity {
    private BluetoothAdapter adapter;
    BluetoothDevice device = null;
    BluetoothService mService = null;
    private TextView tvDeviceName;
    private RadioButton rbSearch;
    private RadioButton rbPrintText;
    private RadioButton rbPrintTicket;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth_demo);
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
                /*String text = "诛仙手游8月10日11时公测\n\n";
                if (text.length() > 0) {
                    mService.sendMessage(text, "GBK");
                }*/
                /*PublicAction action = new PublicAction(this);
                    action.BeforePrintAction();
                    HPRTPrinterHelper.PrintBarCode(
                            70,
                            "623100000051",
                            3,
                            80,
                            2,
                            1);
                    action.AfterPrintAction();*/
                Bitmap qrCode = null;
                try {
                    qrCode = createQRCode("62310000", 200);

                    iv.setImageBitmap(qrCode);
                    Log.e("test", "条形码宽度  " + qrCode.getWidth() + "");
                    //sendMessage(qrCode);

                    PublicAction PAct=new PublicAction(this);
                    PAct.BeforePrintAction();
                    HPRTPrinterHelper.PrintBarCode(65,
                            "623100000012",
                            3,
                            80,
                            2,
                            0);
                    PAct.AfterPrintAction();

                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_rb_print_ticket:

                break;
        }
    }

    private void sendMessage(Bitmap bitmap) {
        // Check that we're actually connected before trying anything
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, "蓝牙没有连接", Toast.LENGTH_SHORT).show();
            return;
        }
        // 发送打印图片前导指令
        byte[] start = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1B,
                0x40, 0x1B, 0x33, 0x00 };
        mService.write(start);

        /**获取打印图片的数据**/
//		byte[] send = getReadBitMapBytes(bitmap);


        byte[] draw2PxPoint = draw2PxPoint(bitmap);

        mService.write(draw2PxPoint);
        // 发送结束指令
        byte[] end = { 0x1d, 0x4c, 0x1f, 0x00 };
        mService.write(end);

    }

    public static byte[] draw2PxPoint(Bitmap bit) {
        byte[] data = new byte[16290];
        int k = 0;
        for (int j = 0; j < 15; j++) {
            data[k++] = 0x1B;
            data[k++] = 0x2A;
            data[k++] = 33; // m=33时，选择24点双密度打印，分辨率达到200DPI。
            data[k++] = 0x68;
            data[k++] = 0x01;
            for (int i = 0; i < 360; i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bit);
                        data[k] += data[k] + b;
                    }
                    k++;
                }
            }
            data[k++] = 10;
        }
        return data;
    }

    /**
     * 图片二值化，黑色是1，白色是0
     */
    public static byte px2Byte(int x, int y, Bitmap bit) {
        byte b;
        int pixel = bit.getPixel(x, y);
        int red = (pixel & 0x00ff0000) >> 16; // 取高两位
        int green = (pixel & 0x0000ff00) >> 8; // 取中两位
        int blue = pixel & 0x000000ff; // 取低两位
        int gray = RGB2Gray(red, green, blue);
        if ( gray < 128 ){
            b = 1;
        } else {
            b = 0;
        }
        return b;
    }

    /**
     * 图片灰度的转化
     */
    private static int RGB2Gray(int r, int g, int b){
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b);  //灰度转化公式
        return  gray;
    }

    /**
     * 把一张Bitmap图片转化为打印机可以打印的bit
     * @param bit
     * @return
     */
    public static byte[] pic2PxPoint(Bitmap bit){
        long start = System.currentTimeMillis();
        byte[] data = new byte[16290];
        int k = 0;
        for (int i = 0; i < 15; i++) {
            data[k++] = 0x1B;
            data[k++] = 0x2A;
            data[k++] = 33; // m=33时，选择24点双密度打印，分辨率达到200DPI。
            data[k++] = 0x68;
            data[k++] = 0x01;
            for (int x = 0; x < 360; x++) {
                for (int m = 0; m < 3; m++) {
                    byte[]  by = new byte[8];
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(x, i * 24 + m * 8 +7-n, bit);
                        by[n] = b;
                    }
                    data[k] = (byte) changePointPx1(by);
                    k++;
                }
            }
            data[k++] = 10;
        }
        long end = System.currentTimeMillis();
        long str = end - start;
        Log.i("TAG", "str:" + str);
        return data;
    }

    /**
     * 将[1,0,0,1,0,0,0,1]这样的二进制转为化十进制的数值（效率更高）
     * @param arry
     * @return
     */
    public static int changePointPx1(byte[] arry){
        int v = 0;
        for (int j = 0; j <arry.length; j++) {
            if( arry[j] == 1) {
                v = v | 1 << j;
            }
        }
        return v;
    }

    private static final int BLACK = 0xff000000;

    public static Bitmap createQRCode(String str, int widthAndHeight) throws WriterException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.EAN_8, widthAndHeight, 80);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
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
