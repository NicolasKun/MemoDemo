package cn.leeq.util.memodemo.ui;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.adapter.GeneralAdapter;
import cn.leeq.util.memodemo.adapter.ViewsHolder;
import cn.leeq.util.memodemo.widget.CommentListView;
import cn.leeq.util.memodemo.widget.SuperSwipeRefresh;

public class RefreshDemo extends BaseActivity implements SuperSwipeRefresh.OnPullRefreshListener, SuperSwipeRefresh.OnPushLoadMoreListener {

    private SuperSwipeRefresh layoutRefresh;
    private CommentListView listView;
    private List<String> data = new ArrayList<>();
    private TextView tvLoadmore;
    private ProgressBar pbLoadmore;
    private static int count = 0;
    private GeneralAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_demo);
        setTitle("下拉刷新|上拉加载");
        initView();

        loadData("This is");
    }

    private void loadData(String text) {
        for (int i = 0; i < 20; i++) {
            data.add(text + " " + i);
        }
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        listView = (CommentListView) findViewById(R.id.rd_listview);
        adapter = new GeneralAdapter<String>(this,data,android.R.layout.simple_list_item_1) {
            @Override
            public void convert(ViewsHolder holder, String item, int position) {
                holder.setText(android.R.id.text1, item);
            }
        };
        listView.setAdapter(adapter);

        layoutRefresh = (SuperSwipeRefresh) findViewById(R.id.rd_swipe_refresh);
        layoutRefresh.setDefaultCircleProgressColor(Color.parseColor("#59b53f"));
        layoutRefresh.setOnPullRefreshListener(this);
        layoutRefresh.setOnPushLoadMoreListener(this);
        layoutRefresh.setFooterView(createFoot());
    }

    private View createFoot() {
        View view = LayoutInflater.from(this).inflate(R.layout.rd_footview, null);
        tvLoadmore = ((TextView) view.findViewById(R.id.rd_loading));
        pbLoadmore = ((ProgressBar) view.findViewById(R.id.rd_progress));
        tvLoadmore.setText("上拉加载更多");
        tvLoadmore.setVisibility(View.VISIBLE);
        pbLoadmore.setVisibility(View.GONE);
        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:  //下拉刷新
                    data.clear();
                    loadData("下拉刷新");
                    layoutRefresh.setRefreshing(false);
                    break;
                case 1:   //上拉加载
                    data.add("上拉加载 " + count);
                    adapter.notifyDataSetChanged();
                    pbLoadmore.setVisibility(View.GONE);
                    layoutRefresh.setLoadMore(false);
                    break;
            }
        }
    };
    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        count = 0;
        handler.sendEmptyMessageDelayed(0, 1200);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {

    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadMore() {
        count++;
        tvLoadmore.setText("正在加载...");
        pbLoadmore.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageDelayed(1, 1200);
    }

    @Override
    public void onPushDistance(int distance) {

    }

    @Override
    public void onPushEnable(boolean enable) {
        tvLoadmore.setText(enable?"松开加载":"上拉加载");
    }


}
