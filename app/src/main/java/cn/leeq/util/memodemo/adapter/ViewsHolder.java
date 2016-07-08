package cn.leeq.util.memodemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by LeeQ on 2016-6-6.
 * 通用adapter的ViewHolder
 */
public class ViewsHolder {
    private final SparseArray<View> mViews;
    private View contentView;
    private int position;
    public ViewsHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.position = position;
        this.mViews = new SparseArray<>();
        contentView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        contentView.setTag(this);
    }

    /**
     * 获得一个ViewsHolder对象
     * @param context   shangxiawen
     * @param convertView  内容控件
     * @param parent     父容器
     * @param layoutId  布局
     * @param position   条目
     * @return
     */
    public static ViewsHolder get(Context context,
                                  View convertView,
                                  ViewGroup parent,
                                  int layoutId,
                                  int position) {
        if (convertView == null) {
            return new ViewsHolder(context, parent, layoutId, position);
        }
        return (ViewsHolder) convertView.getTag();
    }

    /**
     * 通过控件的Id获取相对应的控件 如果没有则加入views
     * @param viewId  控件Id
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = contentView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return contentView;
    }

    //考虑到ListView或者GridView的条目中存在常见的几种控件，遂把他们都整合到ViewHolder中

    /**
     * 为TextView设置文字
     */
    public ViewsHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     */
    public ViewsHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     */
    public ViewsHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置图片
     */
    /*public ViewsHolder setImageForUrl(int viewId, String imgPath) {
        ImageLoader.getInstance().displayImage(imgPath, (ImageView) getView(viewId));
        return this;
    }*/
}
