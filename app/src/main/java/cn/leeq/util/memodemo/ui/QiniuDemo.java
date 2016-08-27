package cn.leeq.util.memodemo.ui;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.utils.QiniuUtil;
import cn.leeq.util.memodemo.utils.RecorderEngine;

public class QiniuDemo extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, MediaRecorder.OnInfoListener, RecorderEngine.RecordListener, RecorderEngine.RecordTimeListener {

    private TextView tvResult,tvLoacalFile,tvInfo;
    private FrameLayout layoutPw;
    private ImageView ivRecorder;
    private TextView rbStartRecorder;
    private Button btnPost;
    private MediaPlayer mediaPlayer;
    private RecorderEngine mRecorderEngine;
    private String mLocalRecorderPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qiniu_demo);
        init();
    }

    private void init() {
        tvResult = (TextView) findViewById(R.id.qd_tv_result);
        tvLoacalFile = (TextView) findViewById(R.id.qd_tv_local_file);
        layoutPw = (FrameLayout) findViewById(R.id.qd_layout_pw);
        ivRecorder = (ImageView) findViewById(R.id.rp_iv_recorder);
        rbStartRecorder = (TextView) findViewById(R.id.qd_rb_start_recorder);
        tvInfo = (TextView) findViewById(R.id.rp_tv_info);
        btnPost = (Button) findViewById(R.id.qd_btn_post);
        btnPost.setOnClickListener(this);
        btnPost.setEnabled(false);
        rbStartRecorder.setOnTouchListener(this);
        ivRecorder.setImageLevel(1);
        mrecorder();
    }

    //初始化录音模块
    private void initVoice() throws FileNotFoundException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mLocalRecorderPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/memo/voice/" + System.currentTimeMillis() / 1000 + ".amr";
            mRecorderEngine = new RecorderEngine(new File(mLocalRecorderPath+"tmp"),this);
            mRecorderEngine.setInfoListener(this);
            mRecorderEngine.setRecordListener(this);
            mRecorderEngine.setRecordTimeListener(this);
        }
    }

    //语音录制
    private void startRecorder() throws IOException {
        //注册音量变化监听
        mRecorderEngine.SetVolumnChangeListener(new RecorderEngine.VolumnChangeListener() {
            @Override
            public void change(int level) {
                Log.e("test", "音量等级 " + level);
                ivRecorder.setImageLevel(level+1);
            }
        });
        //开始录音
        mRecorderEngine.RecordStart();
    }

    private void mrecorder() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

    }
    //上传文件
    public void startPost() {
        String filePath = tvLoacalFile.getText().toString();
        String substring = filePath.substring(filePath.lastIndexOf("/")+1);
        QiniuUtil.upload(filePath,substring, this,handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String result = (String) msg.obj;
                    tvResult.setText(result == null ? "null" : result);
                    btnPost.setEnabled(false);
                    break;
            }
        }
    };


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                layoutPw.setVisibility(View.VISIBLE);

                try {
                    initVoice();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }

                try {
                    startRecorder();
                } catch (IOException e) {
                    mRecorderEngine.RecordStop();
                    mRecorderEngine.RecordDelete();
                    Toast.makeText(QiniuDemo.this, "语音模块初始化失败,请重试", Toast.LENGTH_SHORT).show();
                    break;
                }

                tvInfo.setText("正在录音...");

                break;
            case MotionEvent.ACTION_UP:
                layoutPw.setVisibility(View.GONE);
                mRecorderEngine.RecordStop();
                tvLoacalFile.setText("");

                long recordTime = mRecorderEngine.getRecordTime();
                if (recordTime < 2000) {
                    Toast.makeText(QiniuDemo.this, "录音时间太短", Toast.LENGTH_SHORT).show();
                    mRecorderEngine.RecordStop();
                    mRecorderEngine.RecordDelete();
                    return false;
                } else {
                    String s = mRecorderEngine.RecordGetPath();
                    tvLoacalFile.setText(s);
                    btnPost.setEnabled(true);
                }
                break;
            case MotionEvent.ACTION_CANCEL:  //对于弹出权限申请对话框的处理
                ivRecorder.setImageResource(R.mipmap.record_animate_14);
                if (!mRecorderEngine.ismRecording()) {
                    return false;
                }
                mRecorderEngine.RecordStop();
                mRecorderEngine.RecordDelete();
                layoutPw.setVisibility(View.GONE);
                break;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        startPost();
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {

    }


    @Override
    public void onPrepare() {

    }

    @Override
    public void onComplated() {

    }

    @Override
    public void refresh(long time) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRecorderEngine != null) {
            mRecorderEngine.RecordStop();
            mRecorderEngine.RecordDelete();
        }
    }
}
