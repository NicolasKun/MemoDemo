package cn.leeq.util.memodemo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.adapter.GeneralAdapter;
import cn.leeq.util.memodemo.adapter.ViewsHolder;
import cn.leeq.util.memodemo.widget.NoScorllGridView;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class MultiImageSelect extends AppCompatActivity {

    private Button btnSelect;
    private EditText etNum;
    private NoScorllGridView gridView;
    private int selectNo = 0;
    private int imgsNo = 0;
    private ArrayList<String> pathList=new ArrayList<>();
    private int REQUEST_IMGS = 2;
    private GeneralAdapter<String> adapter;
    private List<String> imgsList = new ArrayList<>();
    private RadioButton rbSingle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_image_select);
        initView();

        btnSelect.setClickable(false);
        rbSingle.setChecked(true);

        if (rbSingle.isChecked()) {
            btnSelect.setClickable(true);
        }
    }

    private void initView() {
        rbSingle = (RadioButton) findViewById(R.id.mis_rb_single);
        btnSelect = (Button) findViewById(R.id.mis_btn_select);
        etNum = (EditText) findViewById(R.id.mis_et_number);
        gridView = (NoScorllGridView) findViewById(R.id.mis_gv_showimg);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }


    public void misSeletImgs(View view) {
        switch (view.getId()) {
            case R.id.mis_btn_select:
                if (etNum.getVisibility() == View.VISIBLE) {
                    if (!TextUtils.isEmpty(etNum.getText())) {
                        String s = etNum.getText().toString();
                        imgsNo = Integer.parseInt(s);
                        //Toast.makeText(MultiImageSelect.this, selectNo + " "+imgsNo, Toast.LENGTH_SHORT).show();
                        if (imgsNo > 0&&imgsNo<=9) {
                            startSelectImgs();
                        }else{
                            Toast.makeText(MultiImageSelect.this, "最多9张~", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MultiImageSelect.this, "尚未输入内容", Toast.LENGTH_SHORT).show();
                    }
                } else if (selectNo == 0) {
                    startSelectImgs();
                }
                break;
            case R.id.mis_rb_multipart:   //多选
                etNum.setVisibility(View.VISIBLE);
                btnSelect.setClickable(true);
                selectNo = 1;
                break;
            case R.id.mis_rb_single:  //单选
                etNum.setVisibility(View.GONE);
                btnSelect.setClickable(true);
                selectNo = 0;
                break;
        }
    }
    private void startSelectImgs() {
        MultiImageSelector multiImageSelector = MultiImageSelector.create();
        if (selectNo == 0) {
            multiImageSelector.single();
        } else if (selectNo == 1 && imgsNo > 0) {
            multiImageSelector.multi()
                    .count(imgsNo);
        }
        if (pathList != null && pathList.size() > 0) {
            multiImageSelector.origin(pathList);
        }
        multiImageSelector.start(this, REQUEST_IMGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMGS) {
            if (resultCode == RESULT_OK) {
                pathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                Log.e("test", "图片选择结果 " + pathList.size());
                adapter = new GeneralAdapter<String>(this,pathList,R.layout.item_mis_gv) {
                    @Override
                    public void convert(ViewsHolder holder, String item, int position) {
                        ImageView view = (ImageView) holder.getView(R.id.item_mis_iv);
                        Log.e("test", "图片地址 " + item);
                        DisplayImageOptions build = new DisplayImageOptions.Builder()
                                .displayer(new RoundedBitmapDisplayer(10))
                                .build();
                        ImageLoader.getInstance().displayImage("file://"+item,view,build);
                    }
                };
                gridView.setAdapter(adapter);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
