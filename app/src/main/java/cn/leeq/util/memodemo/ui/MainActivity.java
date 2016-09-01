package cn.leeq.util.memodemo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.adapter.GeneralAdapter;
import cn.leeq.util.memodemo.adapter.ViewsHolder;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private GridView gridView;
    private List<String> data = new ArrayList<>();
    private long exitTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadData();
    }

    private void loadData() {
        String[] array = getResources().getStringArray(R.array.am_gv);
        for (int i = 0; i < array.length; i++) {
            data.add(array[i]);
        }
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.am_gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(new GeneralAdapter<String>(this,data,R.layout.item_am_gv) {
            @Override
            public void convert(ViewsHolder holder, String item, int position) {
                holder.setText(R.id.item_tv, item);
            }
        });
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                startToActivity(RefreshDemo.class);
                break;
            case 1:
                startToActivity(XUtilsDBDemo.class);
                break;
            case 2:
                startToActivity(MultiImageSelect.class);
                break;
            case 3:
                startToActivity(FriendCircleDemo.class);
                break;
            case 4:
                startToActivity(FlowWordWrapDemo.class);
                break;
            case 5:
//                startToActivity(ChooseLocalFile.class);
                Toast.makeText(MainActivity.this, "暂不开放", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                startToActivity(SecondList.class);
                break;
            case 7:
                startToActivity(StepViewDemo.class);
                break;
            case 8:
                startToActivity(PopupAnimDemo.class);
                break;
            case 9:
                startToActivity(JCVideoPlayerDemo.class);
                break;
            case 10:
                startToActivity(QRDemo.class);
                break;
            case 11:
                startToActivity(NumDateFormatChinese.class);
                break;
            case 12:
                startToActivity(JudgeDate.class);
                break;
            case 13:
                startToActivity(JudgeBtnStateDemo.class);
                break;
            case 14:
                startToActivity(LocationInfoDemo.class);
                break;
            case 15:
                startToActivity(RetrofitDemo.class);
                break;
            case 16:
                startToActivity(BlueToothDemo.class);
                break;
            case 17:
                startToActivity(QiniuDemo.class);
                break;
            case 18:
                startToActivity(IflyVoiceDemo.class);
                break;
        }
    }

    private void startToActivity(Class<?> activity) {
        startActivity(new Intent(this,activity));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            if ((System.currentTimeMillis()-exitTime)>2000){
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime=System.currentTimeMillis();
            }else{
                System.exit(0);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            startToActivity(AutoLoadMore.class);
        }
        return true;
    }
}
