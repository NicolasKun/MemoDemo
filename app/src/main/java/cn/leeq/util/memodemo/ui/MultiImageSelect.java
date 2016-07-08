package cn.leeq.util.memodemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.util.NoScorllGridView;

public class MultiImageSelect extends AppCompatActivity {

    private Button btnSelect;
    private EditText etNum;
    private NoScorllGridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_image_select);
        initView();

        btnSelect.setClickable(false);
    }

    private void initView() {
        btnSelect = (Button) findViewById(R.id.mis_btn_select);
        etNum = (EditText) findViewById(R.id.mis_et_number);
        gridView = (NoScorllGridView) findViewById(R.id.mis_gv_showimg);
    }

    public void misSeletImgs(View view) {
        switch (view.getId()) {
            case R.id.mis_btn_select:
                if (btnSelect.isClickable()) {
                    if (etNum.getVisibility() == View.VISIBLE) {
                        String s = etNum.getText().toString();
                        int imgNo = Integer.parseInt(s);
                        Toast.makeText(MultiImageSelect.this, imgNo + "", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MultiImageSelect.this, "还没有选择模式", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mis_rb_multipart:   //多选
                etNum.setVisibility(View.VISIBLE);

                break;
            case R.id.mis_rb_single:  //单选
                etNum.setVisibility(View.GONE);

                break;
        }
    }
}
