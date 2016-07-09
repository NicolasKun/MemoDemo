package cn.leeq.util.memodemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.leeq.util.memodemo.R;

/**
 * 一个用于演示弹出Activity窗口化的演示Activity
 */
public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

    }

    public void positive(View view) {
        finish();
    }
}
