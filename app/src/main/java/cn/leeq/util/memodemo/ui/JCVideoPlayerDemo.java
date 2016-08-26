package cn.leeq.util.memodemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.config.Constants;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class JCVideoPlayerDemo extends AppCompatActivity {

    private JCVideoPlayerStandard playerStandard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jcvideo_player_demo);
        playerStandard = (JCVideoPlayerStandard) findViewById(R.id.jpd_player);

        playerStandard.setUp(Constants.VIDEO_1, JCVideoPlayer.SCREEN_LAYOUT_LIST, "JAZZ");

    }

    public void playStart(View view) {
        switch (view.getId()) {
            case R.id.jpd_rb_start:
                JCVideoPlayerStandard.startFullscreen(this,
                        JCVideoPlayerStandard.class,
                        Constants.VIDEO_2,
                        "Material Design");
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
