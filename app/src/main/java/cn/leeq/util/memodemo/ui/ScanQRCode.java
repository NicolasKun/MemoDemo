package cn.leeq.util.memodemo.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.yoojia.zxing.qrcode.FinderView;
import com.github.yoojia.zxing.qrcode.QRCodeSupport;

import cn.leeq.util.memodemo.R;

public class ScanQRCode extends AppCompatActivity {

    private SurfaceView surfaceView;
    private FinderView finderView;
    private QRCodeSupport qrCodeSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        init();

        qrCodeSupport = new QRCodeSupport(surfaceView, new QRCodeSupport.OnResultListener() {
            @Override
            public void onScanResult(String notNullResult) {
                //Toast.makeText(ScanQRCode.this, notNullResult, Toast.LENGTH_SHORT).show();
                setResult(201, new Intent().putExtra("scanResult", notNullResult));
                finish();
            }
        });
    }

    private void init() {
        surfaceView = (SurfaceView) findViewById(R.id.sq_surface_view);
        finderView = (FinderView) findViewById(R.id.sq_finder_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeSupport.onResume();
        handler.postDelayed(delayAutoTask, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeSupport.onPause();
        handler.removeCallbacks(delayAutoTask);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private Handler handler = new Handler();

    private final Runnable delayAutoTask = new Runnable() {
        @Override
        public void run() {
            qrCodeSupport.startAuto(500);
        }
    };
}
