package cn.leeq.util.memodemo.ui;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.adapter.GeneralAdapter;
import cn.leeq.util.memodemo.adapter.ViewsHolder;

public class AutoLoadMore extends AppCompatActivity implements AbsListView.OnScrollListener, ViewTreeObserver.OnGlobalLayoutListener {

    private ListView listView;
    private List<String> data = new ArrayList<>();
    private int mLastViewHeight;
    private GeneralAdapter<String> adapter;
    private TextView tvLoadmore;
    private ProgressBar pbLoadmore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_load_more);
        setTitle("自动加载更多");
        listView = (ListView) findViewById(R.id.alm_list_view);

        listView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        listView.setOnScrollListener(this);
        loadData();
    }

    private void loadData() {
        for (int i = 0; i < 15; i++) {
            data.add("第 " + i + " 条");
        }
        adapter = new GeneralAdapter<String>(this,data,android.R.layout.simple_list_item_1) {
            @Override
            public void convert(ViewsHolder holder, String item, int position) {
                holder.setText(android.R.id.text1, item);
            }
        };
        listView.setAdapter(adapter);
    }

    private View createFoot() {
        View view = LayoutInflater.from(this).inflate(R.layout.rd_footview, null);
        tvLoadmore = ((TextView) view.findViewById(R.id.rd_loading));
        pbLoadmore = ((ProgressBar) view.findViewById(R.id.rd_progress));
        tvLoadmore.setText("加载更多");
        tvLoadmore.setVisibility(View.VISIBLE);
        pbLoadmore.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem == 0) {
            View firstVisibleItemView = listView.getChildAt(0);
            if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                Log.e("test", ">>>滚动到顶部<<<");
            }
        }else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
            View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
            if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mLastViewHeight) {
                Log.e("test", "<<<滚动到底部>>>");
                handler.sendEmptyMessageDelayed(0, 1200);
            }
        }
    }

    @Override
    public void onGlobalLayout() {
        mLastViewHeight = listView.getHeight();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }else{
            listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

    private int index = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    for (int i = 0; i < 3; i++) {
                        data.add("加载更多 " + i);
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}
