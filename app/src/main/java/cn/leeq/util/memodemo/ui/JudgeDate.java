package cn.leeq.util.memodemo.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.adapter.GeneralAdapter;
import cn.leeq.util.memodemo.adapter.ViewsHolder;
import cn.leeq.util.memodemo.bean.JudgeBean;
import cn.leeq.util.memodemo.config.Constants;
import cn.leeq.util.memodemo.utils.DateUtils;

public class JudgeDate extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private List<JudgeBean.ListBean> data = new ArrayList<>();
    private TextView tvAddData;
    private GeneralAdapter<JudgeBean.ListBean> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge_date);
        tvAddData = (TextView) findViewById(R.id.jd_tv_add_data);
        listView = (ListView) findViewById(R.id.jd_listview);
        adapter = new GeneralAdapter<JudgeBean.ListBean>(this, data, R.layout.item_judge_list) {
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
        };
        listView.setAdapter(adapter);

        loadData();
        tvAddData.setOnClickListener(this);
    }

    private void loadData() {
        Gson gson = new Gson();
        JudgeBean judgeBean = gson.fromJson(Constants.DATA_FOR_JUDGE, JudgeBean.class);
        data.addAll(judgeBean.getList());
    }

    @Override
    public void onClick(View v) {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_add_memo, null);
        EditText etAge = (EditText) dialogView.findViewById(R.id.dialog_et_age);
        etAge.setVisibility(View.GONE);
        new AlertDialog.Builder(this)
                .setTitle("添加")
                .setView(dialogView)
                .setCancelable(false)
                .setNegativeButton("取消",null)
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addSomePerson(dialogView);
                    }
                }).create().show();
    }

    private void addSomePerson(View dialogView) {
        EditText etName = (EditText) dialogView.findViewById(R.id.dialog_et_name);
        EditText etSigntrue = (EditText) dialogView.findViewById(R.id.dialog_et_signture);

        if (TextUtils.isEmpty(etName.getText()) || TextUtils.isEmpty(etSigntrue.getText())) {
            Toast.makeText(this, "请补充完整", Toast.LENGTH_SHORT).show();
            return;
        }

        String getName = etName.getText().toString();
        String getSign = etSigntrue.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS");
        String format = dateFormat.format(new Date(System.currentTimeMillis()));

        JudgeBean.ListBean bean = new JudgeBean.ListBean();
        bean.setName(getName);
        bean.setContent(getSign);
        bean.setDate(format);

        data.add(bean);
        adapter.notifyDataSetChanged();
    }
}
