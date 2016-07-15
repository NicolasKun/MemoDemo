package cn.leeq.util.memodemo.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import cn.leeq.util.memodemo.R;

public class FriendCircleDemo extends AppCompatActivity {

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_circle_demo);
        tvResult = (TextView) findViewById(R.id.fc_text);

        String string = getString(R.string.format_string, 2, 6);
        tvResult.setText(string);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void select(View view) {
        switch (view.getId()) {
            case R.id.fc_btn_pic:
                startActivityForResult(new Intent()
                        .setType("image/*")
                        .setAction(Intent.ACTION_OPEN_DOCUMENT)
                        .addCategory(Intent.CATEGORY_OPENABLE),
                        100);
                break;
            case R.id.fc_btn_video:
                startActivityForResult(new Intent()
                                .setType("video/*")
                                .setAction(Intent.ACTION_OPEN_DOCUMENT)
                                .addCategory(Intent.CATEGORY_OPENABLE),
                        200);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                String picUri = data.getData().toString() == null ? "" : data.getData().toString();
                File file = new File(picUri);
                tvResult.setText(file.getAbsolutePath());
            }
        }
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                String videoUri = data.getData().toString() == null ? "" : data.getData().toString();

                tvResult.setText(new File(videoUri).getAbsolutePath());
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
