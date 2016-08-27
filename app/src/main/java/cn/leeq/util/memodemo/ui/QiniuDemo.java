package cn.leeq.util.memodemo.ui;

import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.utils.QiniuUtil;

public class QiniuDemo extends AppCompatActivity {

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qiniu_demo);
        init();

    }

    private void init() {
        tvResult = (TextView) findViewById(R.id.qd_tv_result);
    }

    public void startPost(View view) {
        QiniuUtil.upload("filePath","fileName", this,handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String result = (String) msg.obj;
                    tvResult.setText(result == null ? "null" : result);
                    break;
            }
        }
    };

}
