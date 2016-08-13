package cn.leeq.util.memodemo.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zj.btsdk.BluetoothService;

import java.util.Set;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.widget.CommentListView;

public class SelectDevice extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String EXTRA_DEVICE_ADDRESS = "device_found";
    private TextView tvCanbe;
    private TextView tvPaired;
    private CommentListView lvPaired;
    private CommentListView lvCanbe;
    private ArrayAdapter<String> pairedAdapter = null;
    private ArrayAdapter<String> canbeAdapter = null;
    private BluetoothService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);
        init();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);

        service = new BluetoothService(this,null);

        Set<BluetoothDevice> pairedDev = service.getPairedDev();

        if (pairedDev.size() > 0) {
            tvPaired.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDev) {
                pairedAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = "没有已配对的设备".toString();
            pairedAdapter.add(noDevices);
        }
    }

    private void init() {
        tvCanbe = (TextView) findViewById(R.id.sd_tv_canbe_pair);
        tvPaired = (TextView) findViewById(R.id.sd_tv_paired);
        lvPaired = (CommentListView) findViewById(R.id.sd_paired_list_view);
        lvCanbe = (CommentListView) findViewById(R.id.sd_canbe_pair_list_view);
        pairedAdapter = new ArrayAdapter<String>(this, R.layout.device_item);
        canbeAdapter = new ArrayAdapter<String>(this, R.layout.device_item);

        lvCanbe.setAdapter(canbeAdapter);
        lvPaired.setAdapter(pairedAdapter);

        lvCanbe.setOnItemClickListener(this);
        lvPaired.setOnItemClickListener(this);
    }

    public void searchDevice(View view) {
        doSearch();
        view.setVisibility(View.GONE);
    }

    /**
     * 搜索
     */
    private void doSearch() {
        setTitle("正在搜索...");
        tvCanbe.setVisibility(View.VISIBLE);

        if (service.isDiscovering()) {
            service.cancelDiscovery();
        }

        service.startDiscovery();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        service.cancelDiscovery();

        String info = ((TextView) view).getText().toString();
        String address = info.substring(info.length() - 17);

        Log.e("test", "选择的地址 " + address);
        setResult(101, new Intent()
                .putExtra(EXTRA_DEVICE_ADDRESS, address));
        finish();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    canbeAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setTitle("请选择设备连接");
                if (canbeAdapter.getCount() == 0) {
                    String noDevices = "没有发现任何设备".toString();
                    canbeAdapter.add(noDevices);
                }
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (service != null) {
            service.cancelDiscovery();
        }
        service = null;
        unregisterReceiver(mReceiver);
    }
}
