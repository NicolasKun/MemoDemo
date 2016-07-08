package cn.leeq.util.memodemo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.leeq.util.memodemo.R;

public class SecondList extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLabel;
    private int REQUEST_LABEL = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_list);
        tvLabel = (TextView) findViewById(R.id.sl_select_label);
        tvLabel.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LABEL) {
            if (resultCode == 102) {
                String lfCode = data.getStringExtra("lfCode");
                String lfName = data.getStringExtra("lfName");
                String lsCode = data.getStringExtra("lsCode");
                String lsName = data.getStringExtra("lsName");
                tvLabel.setText("一级列表标签 " + lfName + " 编号 " + lfCode + "\n二级列表标签 " + lsName + " 编号 " + lsCode);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(this, SelectLabel.class), REQUEST_LABEL);
    }
}
