package cn.leeq.util.memodemo.ui;

import android.content.Intent;
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
import cn.leeq.util.memodemo.adapter.DynamicAdapter;
import cn.leeq.util.memodemo.adapter.GeneralAdapter;
import cn.leeq.util.memodemo.adapter.ViewsHolder;
import cn.leeq.util.memodemo.bean.DynamicBean;

public class DynamicInsert extends BaseActivity implements AdapterView.OnItemClickListener, DynamicAdapter.OnClickDynamicItemListener {
    private static final String TAG = "DynamicInsert";

    @BindView(R.id.di_list_view)
    ListView diListView;

    private List<DynamicBean> lvData = new ArrayList<>();
    private DynamicAdapter mListAdapter;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_insert);
        ButterKnife.bind(this);
        loadData();
        init();
    }

    private void init() {
        View listBottomLayout = LayoutInflater.from(this).inflate(R.layout.bottom_layout_dynamic_insert, null);
        diListView.addFooterView(listBottomLayout);

        mListAdapter = new DynamicAdapter(lvData, this, this);
        diListView.setAdapter(mListAdapter);

        diListView.setOnItemClickListener(this);
    }

    private void loadData() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == lvData.size()) {
            lvData.add(new DynamicBean());
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onScan(int position) {
        mPosition = position;
        startActivityForResult(new Intent(this, CaptureActivity.class), 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                lvData.get(mPosition).setContent(result);
                mListAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
