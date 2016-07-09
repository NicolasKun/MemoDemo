package cn.leeq.util.memodemo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.widget.DDPopupWindow;

/**
 * 弹出动画演示
 */
public class PopupAnimDemo extends AppCompatActivity implements DDPopupWindow.setOnClickPopupWindowListener {

    private DDPopupWindow ddPopupWindow;
    private RadioButton rbPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_anim_demo);
        ddPopupWindow = new DDPopupWindow(this,this);
        rbPw = (RadioButton) findViewById(R.id.pa_rb_pw);

    }

    public void popupPopup(View view) {
        switch (view.getId()) {
            case R.id.pa_rb_activity:  //弹出Activity
                startActivity(new Intent(this,DialogActivity.class));
                break;
            case R.id.pa_rb_pw:  //弹出popupWindow
                ddPopupWindow.showPopupWindow(rbPw);
                break;
        }
    }

    @Override
    public void onClickZan(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PopupAnimDemo.this, "赞", Toast.LENGTH_SHORT).show();
                ddPopupWindow.dismiss();
            }
        });
    }

    @Override
    public void onClickComment(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PopupAnimDemo.this, "评论", Toast.LENGTH_SHORT).show();
                ddPopupWindow.dismiss();
            }
        });
    }
}
