package cn.leeq.util.memodemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.config.Constants;
import cn.leeq.util.memodemo.util.WordWrapView;

public class FlowWordWrapDemo extends AppCompatActivity {

    private WordWrapView wrapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_word_wrap_demo);
        wrapView = (WordWrapView) findViewById(R.id.fww_wordwrap);

        for (int i = 0; i < Constants.WORD_WRAP_TEST_LABLE.length; i++) {
            TextView tv = new TextView(this);
            tv.setText(Constants.WORD_WRAP_TEST_LABLE[i]);
            tv.setBackgroundResource(Constants.textColors[i]);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(15,15,15,15);
            if (wrapView.getChildCount() < Constants.WORD_WRAP_TEST_LABLE.length) {
                wrapView.addView(tv);
            }
        }


    }
}
