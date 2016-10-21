package cn.leeq.util.memodemo.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.adapter.GeneralAdapter;
import cn.leeq.util.memodemo.adapter.ViewsHolder;

public class DynamicInsert extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "DynamicInsert";

    @BindView(R.id.di_list_view)
    ListView diListView;
    @BindView(R.id.di_grid_view)
    GridView diGridView;

    private List<String> lvData = new ArrayList<>();
    private GeneralAdapter<String> lvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_insert);
        ButterKnife.bind(this);
        loadData();
        init();
    }

    private void init() {
        diListView.setAdapter(lvAdapter = new GeneralAdapter<String>(this, lvData, R.layout.item_dynamic_insert_list) {
            @Override
            public void convert(ViewsHolder holder, String item, int position) {
                holder.setText(R.id.item_di_list_tv_content, item);
            }
        });

        View listBottomLayout = LayoutInflater.from(this).inflate(R.layout.bottom_layout_dynamic_insert, null);
        diListView.addFooterView(listBottomLayout);

        diListView.setOnItemClickListener(this);
    }

    private void loadData() {
        for (int i = 1; i < 3; i++) {
            lvData.add("不想再吃第 " + i + " 顿了...");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e(TAG, "onItemClick: " + position);
        if (position == lvData.size()) {
            lvData.add("不想吃   " + (lvData.size() + 1) + " 顿了...");
            lvAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, position + " -- ", Toast.LENGTH_SHORT).show();
        }
    }
}
