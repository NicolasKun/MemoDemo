package cn.leeq.util.memodemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.leeq.util.memodemo.R;

/**
 * 点击按钮TextView改变状态
 * 这里改变的是selected状态
 *
 * 补充 - focus select区别
 *  · focus 获取焦点  一个界面只能有一个focus状态
 *  · select 一个界面可以有很多select状态
 */
public class JudgeBtnStateDemo extends AppCompatActivity implements View.OnClickListener {

    private TextView tv1,tv2,tv3;
    private int index = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge_btn_state_demo);
        init();


        tv1.setSelected(false);
        tv2.setSelected(false);
        tv3.setSelected(false);
        clickForTextView();
    }

    private void clickForTextView() {
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
    }

    private void init() {
        tv1 = (TextView) findViewById(R.id.jbs_tv_1);
        tv2 = (TextView) findViewById(R.id.jbs_tv_2);
        tv3 = (TextView) findViewById(R.id.jbs_tv_3);
    }

    public void startJudge(View view) {
        index++;
        if (index % 2 == 0) {
            tv1.setSelected(true);
            tv2.setSelected(true);
            tv3.setSelected(true);
            tv1.setClickable(true);
            tv2.setClickable(true);
            tv3.setClickable(true);
        } else if (index % 2 == 1) {
            tv1.setSelected(false);
            tv2.setSelected(false);
            tv3.setSelected(false);
            tv1.setClickable(false);
            tv2.setClickable(false);
            tv3.setClickable(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jbs_tv_1:
                if (tv1.isSelected()) {
                    Toast.makeText(JudgeBtnStateDemo.this, tv1.getText(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.jbs_tv_2:
                if (tv2.isSelected()) {
                    Toast.makeText(JudgeBtnStateDemo.this, tv2.getText(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.jbs_tv_3:
                if (tv3.isSelected()) {
                    Toast.makeText(JudgeBtnStateDemo.this, tv3.getText(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
