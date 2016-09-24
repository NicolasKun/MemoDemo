package cn.leeq.util.memodemo.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.config.Constants;
import cn.leeq.util.memodemo.utils.QiniuUtil;
import cn.leeq.util.memodemo.utils.RecorderEngine;

public class QiniuDemo extends BaseActivity implements View.OnTouchListener, View.OnClickListener, MediaRecorder.OnInfoListener, RecorderEngine.RecordListener, RecorderEngine.RecordTimeListener {

    private TextView tvResult,tvLoacalFile,tvInfo,rbStartRecorder;
    private FrameLayout layoutPw;
    private ImageView ivRecorder;
    private Button btnPost;
    private MediaPlayer mediaPlayer;
    private RecorderEngine mRecorderEngine;
    private String mLocalRecorderPath;
    private RelativeLayout layoutPlayAudio;
    private ContentLoadingProgressBar pbPlayAudio;
    private RadioButton rbPlayAudio;
    private String mLocalKey;
    private String mDownloadVoicePath;
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
        layoutPlayAudio = (RelativeLayout) findViewById(R.id.qd_layout_play);
        pbPlayAudio = (ContentLoadingProgressBar) findViewById(R.id.qd_progress_bar_play);
        rbPlayAudio = (RadioButton) findViewById(R.id.qd_rb_play_audio);
        btnPost.setOnClickListener(this);
        rbPlayAudio.setOnClickListener(this);
        btnPost.setEnabled(false);
        rbStartRecorder.setOnTouchListener(this);
        ivRecorder.setImageLevel(1);
        pbPlayAudio.getIndeterminateDrawable().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#59b53f"), PorterDuff.Mode.SRC_ATOP));

        layoutPlayAudio.setVisibility(View.GONE); //初始化时隐藏
    }

    //初始化录音模块
    private void initVoice() throws FileNotFoundException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mLocalRecorderPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/memo/voice/" + System.currentTimeMillis() / 1000 + ".mp3";
            mRecorderEngine = new RecorderEngine(new File(mLocalRecorderPath),this);
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
                ivRecorder.setImageLevel(level+1);
            }
        });
        //开始录音
        mRecorderEngine.RecordStart();
    }

    //上传文件
    public void startPost() {
        String filePath = tvLoacalFile.getText().toString();
        mLocalKey = filePath.substring(filePath.lastIndexOf("/") + 1);
        QiniuUtil.upload(filePath, mLocalKey, this, handler);
        btnPost.setEnabled(false);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String result = (String) msg.obj;
                    tvResult.setText(result == null ? "null" : result);
                    layoutPlayAudio.setVisibility(View.VISIBLE);
                    pbPlayAudio.setVisibility(View.VISIBLE);
                    rbPlayAudio.setVisibility(View.GONE);

                    downloadAndPlay();
                    break;
                case 1:
                    String error = (String) msg.obj;
                    tvResult.setText(error);
                    break;
                case 2:
                    File downResult = (File) msg.obj;
                    String absolutePath = downResult.getAbsolutePath();

                    Log.e("test", "下载成功 " + absolutePath);
                    mDownloadVoicePath = absolutePath;
                    pbPlayAudio.setVisibility(View.GONE);
                    rbPlayAudio.setVisibility(View.VISIBLE);
                    break;
                case 99:
                    Toast.makeText(QiniuDemo.this, "下载失败", Toast.LENGTH_SHORT).show();
                    layoutPlayAudio.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void downloadAndPlay() {
        //Constants.DOMAIN + File.separator + mLocalKey
        RequestParams params = new RequestParams(Constants.DOMAIN + File.separator + mLocalKey);
        params.setAutoResume(true);
        params.setSaveFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/memo/download/" + mLocalKey.substring(0, mLocalKey.lastIndexOf(".") + 4));
        loadByXUtils(params, handler, 2);
    }


    /**
     * 录音按钮触摸事件的处理
     */
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

    /**
     * 点击POST按钮
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qd_btn_post:
                startPost();
                //FIXME 微信Url Scheme
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                ComponentName cn = new ComponentName("com.tencent.mm", "com.tencent.mm.plugin.base.stub.WXCustomSchemeEntryActivity");
                intent.setData(Uri.parse("javascript:window.location.href='weixin://dl/favorites'"));
                intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                intent.setComponent(cn);
                startActivity(intent);*/
                break;
            case R.id.qd_rb_play_audio:
                //Toast.makeText(QiniuDemo.this, mDownloadVoicePath+"", Toast.LENGTH_SHORT).show();
                startPlay(mDownloadVoicePath);
                break;
        }
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

    /**
     * LifeCircle
     */
    @Override
    protected void onStop() {
        super.onStop();
        stopPlaying();
        stopRecorder();
    }

    private void stopRecorder() {
        if (mRecorderEngine != null) {
            mRecorderEngine.RecordStop();
            mRecorderEngine.RecordDelete();
        }
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void startPlay(String filePath) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
