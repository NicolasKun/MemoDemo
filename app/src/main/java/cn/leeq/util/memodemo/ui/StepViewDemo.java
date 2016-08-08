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

import com.baoyachi.stepview.HorizontalStepView;
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
    private HorizontalStepView hStepView;
    private List<String> list = new ArrayList<>();
    private List<String> labelsDate = new ArrayList<>();
    private StepsView stepsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_view_demo);
        stepsView = (StepsView) findViewById(R.id.svd_step_view);
        etStep = (EditText) findViewById(R.id.svd_et_step);
        hStepView = (HorizontalStepView) findViewById(R.id.svd_byc_horizontal_stepview);
        loadData();

        loadHorizontalStepView();

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

        Type type = new TypeToken<List<Status>>() {
        }.getType();
        List<Status> temp = new Gson().fromJson(Constants.DATE_LABELS, type);
        for (Status status : temp) {
            int id = status.getId();
            String create_date = status.getCreate_date();
            String date_day = create_date.substring(0, create_date.indexOf(" "));
            String date_min = create_date.substring(create_date.indexOf(" ") + 1, create_date.indexOf("."));
            Log.e("test", "截取日期 " + date_day + "\n截取分钟 " + date_min);
            labelsDate.add(date_min + "\n" + date_day);
        }


    }


    /**
     * 包牙齿的Stepview
     */
    private void loadHorizontalStepView() {

        hStepView.setStepsViewIndicatorComplectingPosition(2)
                .setStepViewTexts(list)
                .setTextSize(10)
                //完成线的颜色
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(this, R.color.colorPrimary))
                //未完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.colorGray))
                //完成后文字的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                //未完成文字的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, R.color.colorGray))
                //完成后指示器的样式
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.shape_svd_circle_green))
                //指示器默认的样式
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.shape_svd_circle_nor));
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
