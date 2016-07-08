package cn.leeq.util.memodemo.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.anton46.stepsview.StepsView;

import cn.leeq.util.memodemo.R;

public class StepViewDemo extends AppCompatActivity {

    private StepsView stepsView;
    private String labels[] = {"s1","s2","s3","s4","s5","s6","s7"};
    private EditText etStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_view_demo);
        etStep = (EditText) findViewById(R.id.svd_et_step);
        stepsView = (StepsView) findViewById(R.id.svd_stepview);

        stepsView.setLabels(labels)
                .setBarColorIndicator(Color.parseColor("#DBDBDB"))
                .setProgressColorIndicator(Color.parseColor("#59b53f"))
                .setCompletedPosition(0)
                .drawView();
    }

    public void positive(View view) {
        if (!TextUtils.isEmpty(etStep.getText())) {
            int step = Integer.parseInt(etStep.getText().toString());
            if (step > labels.length-1) {
                Toast.makeText(StepViewDemo.this, "太长了", Toast.LENGTH_SHORT).show();
            }else{
                stepsView.setCompletedPosition(step).drawView();
                etStep.setText("");
            }
        }else{
            Toast.makeText(StepViewDemo.this, "请输入步长", Toast.LENGTH_SHORT).show();
        }
    }
}
