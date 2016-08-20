package cn.leeq.util.memodemo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.widget.PCDPopUpWindow;

public class SecondList extends AppCompatActivity implements View.OnClickListener, PCDPopUpWindow.OnCheckChangeListener {

    private TextView tvLabel;
    private int REQUEST_LABEL = 101;
    private RadioButton rbCity;
    private TextView tvCity;
    private PCDPopUpWindow popUpWindow;
    private String city;
    private String province;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_second_list);
        tvLabel = (TextView) findViewById(R.id.sl_select_label);
        rbCity = (RadioButton) findViewById(R.id.sl_rb_select_city);
        tvCity = (TextView) findViewById(R.id.sl_tv_select_city);
        tvLabel.setOnClickListener(this);
        rbCity.setOnClickListener(this);
        popUpWindow = new PCDPopUpWindow(this,"addr.db",this);
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
        switch (v.getId()) {
            case R.id.sl_select_label:
                startActivityForResult(new Intent(this, SelectLabel.class), REQUEST_LABEL);
                break;
            case R.id.sl_rb_select_city:
                popUpWindow.showAsDropDown(rbCity);
                break;
        }
    }

    //回调回来的城市名称及其Id
    @Override
    public void setCityText(String cityText, int cityId) {
        city = cityText;
    }
    //省
    @Override
    public void setProvinceText(String provinceText, int provinceId) {
        province = provinceText;
    }

    //地区
    @Override
    public void setDistrictText(String areaText, int areaId) {
        String addr = province + "\t" + city + "\t" + areaText;
        tvCity.setText(addr);
    }
}
