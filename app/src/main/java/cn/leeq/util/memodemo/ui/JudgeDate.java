package cn.leeq.util.memodemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.adapter.GeneralAdapter;
import cn.leeq.util.memodemo.adapter.ViewsHolder;
import cn.leeq.util.memodemo.bean.JudgeBean;
import cn.leeq.util.memodemo.config.Constants;
import cn.leeq.util.memodemo.utils.DateUtils;

public class JudgeDate extends AppCompatActivity {

    private ListView listView;
    private List<JudgeBean.ListBean> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge_date);
        listView = (ListView) findViewById(R.id.jd_listview);

        listView.setAdapter(new GeneralAdapter<JudgeBean.ListBean>(this,data,R.layout.item_judge_list) {
            @Override
            public void convert(ViewsHolder holder, JudgeBean.ListBean item, int position) {
                holder.setText(R.id.item_jl_tv_content, item.getContent());
                holder.setText(R.id.item_jl_tv_name, item.getName());
                try {
                    String formatDate = DateUtils.convertTimeToFormat(item.getDate());
                    holder.setText(R.id.item_jl_tv_date, formatDate);
                } catch (ParseException e) {
                    Log.e("test", "日期异常 " + e.getMessage());
                }
            }
        });
        loadData();
    }

    private void loadData() {
        Gson gson = new Gson();
        JudgeBean judgeBean = gson.fromJson(Constants.DATA_FOR_JUDGE, JudgeBean.class);
        data.addAll(judgeBean.getList());
    }
}
