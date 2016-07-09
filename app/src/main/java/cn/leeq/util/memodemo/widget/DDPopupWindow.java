package cn.leeq.util.memodemo.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import cn.leeq.util.memodemo.R;


/**
 * Created by 云星空科技 on 2016/4/5.
 */
public class DDPopupWindow extends PopupWindow  {
    private View contentView;
    private Button btnZan,btnPingLun;
    private setOnClickPopupWindowListener onclick;
    public DDPopupWindow(final Activity context ,setOnClickPopupWindowListener onclick) {
        super(context);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView=inflater.inflate(R.layout.layout_popupwin,null);
        btnPingLun= (Button) contentView.findViewById(R.id.btn_pinglun);
        btnZan= (Button) contentView.findViewById(R.id.btn_zan);



        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(70);
        this.setFocusable(true);
        this.setOutsideTouchable(true);

        this.update();

        this.setAnimationStyle(R.style.popupWindow);
        ColorDrawable dw=new ColorDrawable(0);
        this.setBackgroundDrawable(dw);

        onclick.onClickZan(btnZan);
        onclick.onClickComment(btnPingLun);
    }




    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            View view=this.getContentView();
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            this.showAsDropDown(parent,parent.getWidth()-view.getMeasuredWidth(),-parent.getHeight()-20);
        }else{
            this.dismiss();
        }
    }

    public interface setOnClickPopupWindowListener{
        void onClickZan(View v);
        void onClickComment(View v);
    }
}
