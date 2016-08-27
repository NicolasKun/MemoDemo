package cn.leeq.util.memodemo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Handler;
import android.util.Log;

/**
 * 录音的工具类
 *
 * @author xiaoyf
 *
 * @createTime 2016年1月27日 下午3:54:42
 */
public class RecorderEngine {
    final int MIN_AMPLITUDE_VALUE = 800; // 录音程序可以识别的最小声音振幅
    final int MAX_AMPLITUDE_VALUE = 32000;// 最大值有人说[32767]; // 录音程序可以识别的最大声音振幅
    final int VOLUMN_LEVEL_MAX = 10; // 音量分成7级
    final int ampPerLevel = (MAX_AMPLITUDE_VALUE - MIN_AMPLITUDE_VALUE)
            / VOLUMN_LEVEL_MAX;
    private int mVolumnLevel = 0;

    private File mRecordFile = null;
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer = null;
    private boolean mRecording = false;
    private VolumnChangeListener mVolumnChangeListener;
    private OnInfoListener mInfoListener;
    private RecordTimeListener recordTimeListener;
    private AudioManager audioManager;
    private RecordListener listener;

    private Context context;

    // 录音音量变化侦听接口
    public interface VolumnChangeListener {
        public void change(int level);
    }

    public void SetVolumnChangeListener(VolumnChangeListener listener) {
        mVolumnChangeListener = listener;
    }

    public void setInfoListener(OnInfoListener mInfoListener) {
        this.mInfoListener = mInfoListener;
    }

    public interface RecordTimeListener {
        void refresh(long time);
    }

    public void setRecordTimeListener(RecordTimeListener listener) {
        recordTimeListener = listener;
    }

    /**
     * 根据频率计算音量级别
     *
     * @return 音量级别
     */
    private int CaculateVolumnLevel() {
        int ratio = mMediaRecorder.getMaxAmplitude() / 600;
        int db = 0;// 分贝
        if (ratio > 1)
            db = (int) (20 * Math.log10(ratio));
        db /= 3;
        db = Math.min(db, 10);
        return db;
    }

    // 录音定时器，更新状态
    private int mTimingDelay = 100;
    private long mRecordTime = 0;
    Handler timingHandler = new Handler();
    /**
     * 更新音量级别
     */
    Runnable timingRunnable = new Runnable() {
        public void run() {
            int newLevel = CaculateVolumnLevel();
            if (mVolumnLevel != newLevel) {
                mVolumnLevel = newLevel;
                if (mVolumnChangeListener != null)
                    mVolumnChangeListener.change(mVolumnLevel);
            }

            // 循环启动定时器
            timingHandler.postDelayed(timingRunnable, mTimingDelay);
        }
    };
    /**
     * 记录录音的时间
     */
    Runnable recordTimeRunnable = new Runnable() {

        @Override
        public void run() {
            if (recordTimeListener != null) {
                long time = System.currentTimeMillis() - mRecordTime;
                recordTimeListener.refresh(time);
                // 循环启动定时器
                timingHandler.postDelayed(recordTimeRunnable, 950);
            }
        }
    };

    private void startTiming() {
        mRecordTime = System.currentTimeMillis() + 800L;
        timingHandler.postDelayed(timingRunnable, mTimingDelay);
        timingHandler.postDelayed(recordTimeRunnable, 950);
    }

    private void stopTiming() {
        mRecordTime = System.currentTimeMillis() - mRecordTime;
        timingHandler.removeCallbacks(timingRunnable);
        timingHandler.removeCallbacks(recordTimeRunnable);
    }

    /**
     * 获取本次录音时间长度
     *
     * @return
     */
    public long getRecordTime() {
        return mRecordTime;
    }

    public RecorderEngine(Context mContext) throws FileNotFoundException {
        this(null, mContext);
        this.context = mContext;
    }

    public RecorderEngine(File recordFile, Context mContext)
            throws FileNotFoundException {
        if (recordFile != null) {
            File dir = recordFile.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 创建文件
            if (!recordFile.exists()) {
                try {
                    recordFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        mRecordFile = recordFile;
        mRecordTime = System.currentTimeMillis();
        audioManager = (AudioManager) mContext
                .getSystemService(Service.AUDIO_SERVICE);
    }

    public void setRecordListener(RecordListener listener) {
        this.listener = listener;
    }

    /* 录音 */
    public void RecordStart() throws IllegalStateException, IOException,
            RuntimeException {
        if (listener != null) {
            listener.onPrepare();
        }
        mMediaRecorder = new MediaRecorder();
        startTiming();
        // AudioRecord audioRecord=new AudioRecord(audioSource, sampleRateInHz,
        // channelConfig, audioFormat, bufferSizeInBytes)
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 声源
        // 设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 编码
        mMediaRecorder.setAudioEncodingBitRate(4000);// 比特率
        // mMediaRecorder.setAudioSamplingRate(4000);// 采样率
        mMediaRecorder.setAudioChannels(1);// 单声道
        mMediaRecorder.setOnInfoListener(mInfoListener);
        mMediaRecorder.setOutputFile(mRecordFile.getAbsolutePath());
        mMediaRecorder.setMaxDuration(60 * 1000);
        mMediaRecorder.prepare();
        mMediaRecorder.start();
        mRecording = true;
    }

    public void RecordStop() {
        if (mRecordFile != null) {
            if (mMediaRecorder != null) {
                try {
                    mMediaRecorder.stop();
                    mMediaRecorder.release();
                    mMediaRecorder = null;
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mRecording = false;
            stopTiming();
        }
        if (listener != null) {
            listener.onComplated();
        }
        listener = null;
    }

    public void RecordPlay(Context mContext) throws IllegalArgumentException,
            SecurityException, IllegalStateException, IOException {
        // FileInputStream audioFileStream = new FileInputStream(mRecordFile);
        if (mRecordFile == null) {
        } else {
            mMediaPlayer = new MediaPlayer();
            // mMediaPlayer = MediaPlayer.create(mContext,
            // Uri.fromFile(mRecordFile));
            // mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(mRecordFile.getAbsolutePath());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        }
    }

    public int getSuitedVolume(int streamType) {
        int volume = 10;
        if (audioManager != null) {
            int currentVolume = audioManager.getStreamVolume(streamType);
            int tmpVolume = (int) (audioManager.getStreamMaxVolume(streamType) * 0.6);
            // 确保音量至少为60%
            volume = currentVolume >= tmpVolume ? currentVolume
                    : (tmpVolume > volume ? tmpVolume : volume);
        }
        return volume;
    }

    public void RecordPlayStop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 设置播放音量
     *
     * @param volume
     *            音量，如果设置为-1则设置为当前系统音量
     * */
    public void setRecordPlayerVolume(int volume) {
        if (audioManager != null) {
            volume = volume == -1 ? audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC) : volume;
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    volume, AudioManager.FLAG_PLAY_SOUND);
        }
    }

    public String RecordGetPath() {
        String path = mRecordFile.getPath();
        return path;
    }

    public void RecordDelete() {
        if (mRecordFile != null) {
            if (mRecordFile.exists())
                mRecordFile.delete();
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public boolean ismRecording() {
        return mRecording;
    }

    public interface RecordListener {
        public void onPrepare();

        public void onComplated();
    }
}
