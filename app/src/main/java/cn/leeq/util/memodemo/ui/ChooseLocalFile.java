package cn.leeq.util.memodemo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.activity.HomeScreenMediaChooser;

import java.util.ArrayList;

import cn.leeq.util.memodemo.R;

public class ChooseLocalFile extends AppCompatActivity {

    private TextView tvPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_local_file);
        init();

        IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(videoBroadcastReceiver, videoIntentFilter);
    }

    private void init() {
        tvPath = (TextView) findViewById(R.id.clf_tv_video_path);
    }

    public void chooseVideo(View view) {
        HomeScreenMediaChooser.startMediaChooser(this, false);
    }

    BroadcastReceiver videoBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("test", "video size " + intent.getStringArrayListExtra("list").size());
            ArrayList<String> list = intent.getStringArrayListExtra("list");
            for (String s : list) {
                tvPath.setText(s);
            }
        }
    };


    @Override
    protected void onDestroy() {
        unregisterReceiver(videoBroadcastReceiver);
        super.onDestroy();
    }
}
