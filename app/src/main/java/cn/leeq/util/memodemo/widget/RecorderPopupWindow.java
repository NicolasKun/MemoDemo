package cn.leeq.util.memodemo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.leeq.util.memodemo.R;

/**
 * Created by LeeQ
 * Date : 2016-08-27
 * Name : MemoDemo
 * Use :
 */
public class RecorderPopupWindow extends PopupWindow {

    private final View contentView;
    private final ImageView ivRecorder;
    private final TextView tvInfo;

    public RecorderPopupWindow(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.layout_recorder_pw,null);
        ivRecorder = (ImageView) contentView.findViewById(R.id.rp_iv_recorder);
        tvInfo = (TextView) contentView.findViewById(R.id.rp_tv_info);

        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);

        this.update();

        this.setBackgroundDrawable(new ColorDrawable(0));


    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 0, Gravity.CENTER);
        }else{
            this.dismiss();
        }
    }


    public void setImageLevel(int level) {
        this.ivRecorder.setImageLevel(level);
    }
}
