package cn.leeq.util.memodemo.ui;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.http.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.bean.Status;
import cn.leeq.util.memodemo.config.Constants;
import cn.leeq.util.memodemo.widget.StepsView;
import retrofit2.Retrofit;


public class StepViewDemo extends AppCompatActivity {

    private EditText etStep;
    private List<String> list = new ArrayList<>();
    private List<String> labelsDate = new ArrayList<>();
    private StepsView stepsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_view_demo);
        stepsView = (StepsView) findViewById(R.id.svd_step_view);
        etStep = (EditText) findViewById(R.id.svd_et_step);
        loadData();

        /**
         * 魔改anton46的stepview
         * 0.0
         */
        stepsView.setLabels(list)
                .setLabelsDate(labelsDate)
                .setBarColorIndicator(Color.parseColor("#37474f"))
                .setProgressColorIndicator(Color.parseColor("#59b53f"))
                .setLabelColorIndicator(Color.parseColor("#59b53f"))
                .drawView();

    }

    private void loadData() {
        list.add("提交订单");
        list.add("网点受理");
        list.add("快递员取件");
        list.add("快递员完成收件");
        list.add("网点揽收");
        list.add("网点发件");
        list.add("网店到件");
        list.add("快递员派件");
        list.add("快递员已派件");
        list.add("签收");
    }


    public void positive(View view) {
        if (!TextUtils.isEmpty(etStep.getText())) {
            int step = Integer.parseInt(etStep.getText().toString());
            if (step > list.size()-1) {
                Toast.makeText(StepViewDemo.this, "太长了", Toast.LENGTH_SHORT).show();
            }else{
                stepsView.setCompletedPosition(step)
                        .drawView();
                etStep.setText("");
            }
        }else{
            Toast.makeText(StepViewDemo.this, "请输入步长", Toast.LENGTH_SHORT).show();
        }
    }


}
