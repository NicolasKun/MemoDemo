package cn.leeq.util.memodemo.ui;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.config.Constants;

public class IflyVoiceDemo extends AppCompatActivity {

    private Toast              mToast;
    private String             sContent;
    private EditText           etContent;
    private SpeechSynthesizer  mTts;

    //合成进度
    private int mIndexOfPlaying   = 0;
    private int mIndexOfBuffering = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ifly_voice_demo);

        init();
    }

    private void init() {
        etContent = (EditText) findViewById(R.id.if_ed_content);
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
        etContent.setText(getResources().getString(R.string.ifly_et_default));
        sContent = getIntent().getStringExtra("speech");

        if (!Constants.BD_PUSH_CONTENT.equals("")) {
            startPlay();
        }
    }

    private void startPlay() {
        setParams();
        int code = mTts.startSpeaking(Constants.BD_PUSH_CONTENT, mTtsLinstener);

        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED)
                showToast("没有安装");
            else
                showToast("语音合成失败 " + code);
        }
    }

    //在线合成语音并播放
    public void play(View view) {
        String content = etContent.getText().toString();
        setParams();
        int code = mTts.startSpeaking(content, mTtsLinstener);

        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED)
                showToast("没有安装");
            else
                showToast("语音合成失败 " + code);
        }
    }

    //设置参数
    private void setParams() {
        //清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //根据合成影城设置相应参数  这里只是在线合成
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mTts.setParameter(SpeechConstant.VOICE_NAME, "aisxmeng");  //设置合成发音人
        mTts.setParameter(SpeechConstant.SPEED, "50"); //语速
        mTts.setParameter(SpeechConstant.PITCH, "50"); //音调
        mTts.setParameter(SpeechConstant.VOLUME, "50"); //音量

        //设置播放音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        //播放合成音频打断音乐播放
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        //设置音频保存路径 格式wav pcm
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        long l = System.currentTimeMillis() / 1000;
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/memo/msc/" + l + ".wav");
    }

    //初始化监听
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int i) {
            Log.e("test", "初始化监听 " + i);
            if (i != ErrorCode.SUCCESS) {
                showToast("初始化失败 " + i);
            } else {
                //FIXME 开始讲话
            }
        }
    };

    //展示Toast
    private void showToast(String string) {
        mToast.setText(string);
        mToast.show();
    }

    /**
     * 合成语音回调
     */
    private SynthesizerListener mTtsLinstener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            showToast("开始播放");
        }

        //合成进度
        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
            mIndexOfBuffering = i;
            showToast(String.format("缓冲进度 %d%%,播放进度 %d%%", mIndexOfBuffering, mIndexOfPlaying));
        }

        //播放进度
        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
            mIndexOfPlaying = i;
            showToast(String.format("缓冲进度 %d%%,播放进度 %d%%", mIndexOfBuffering, mIndexOfPlaying));
        }

        @Override
        public void onSpeakPaused() {
            showToast("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showToast("继续播放");
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            if (speechError == null) {
                showToast("播放完成");
            } else {
                showToast(speechError.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
            if (SpeechEvent.EVENT_SESSION_ID == i) {
                String sid = bundle.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                Log.d("test", "session id =" + sid);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTts.isSpeaking()) {
            mTts.stopSpeaking();
        }
        mTts.destroy();
    }

}
