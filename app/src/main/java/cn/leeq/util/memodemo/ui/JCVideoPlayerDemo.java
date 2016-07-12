package cn.leeq.util.memodemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.leeq.util.memodemo.R;
import fm.jiecao.jcvideoplayer_lib.JCFullScreenActivity;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class JCVideoPlayerDemo extends AppCompatActivity {

    private JCVideoPlayerStandard player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jcvideo_player_demo);
        player = (JCVideoPlayerStandard) findViewById(R.id.jpd_player);

        player.setUp("http://fengss.com:80//uploads/13412345678/1467277546001_339255863.MP4", "谷歌设计");

        ImageLoader.getInstance().displayImage("http://fengss.com:80/uploads/13412345678/1467277546079_27347417.jpg",player.thumbImageView);
    }

    public void playStart(View view) {
        switch (view.getId()) {
            case R.id.jpd_rb_start:
                JCFullScreenActivity.startActivity(this,
                        "http://fengss.com:80//uploads/13412345678/1468071524355_228084703.mp4",
                        JCVideoPlayerStandard.class,
                        "客户视频");
                break;
        }
    }

    @Override
    protected void onPause() {
        player.releaseAllVideos();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            player.releaseAllVideos();
        }
        return super.onKeyDown(keyCode, event);
    }
}
