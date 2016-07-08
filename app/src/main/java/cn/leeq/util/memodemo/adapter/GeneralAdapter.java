package cn.leeq.util.memodemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by LeeQ on 2016-6-6.
 * 通用的Adapter
 */
public abstract class GeneralAdapter<T> extends BaseAdapter {
    protected LayoutInflater inflater;
    protected Context context;
    protected List<T> data;
    protected final int itemLayoutId;
    public GeneralAdapter(Context context, List<T> data, int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewsHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(viewHolder,getItem(position),position);
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewsHolder holder, T item,int position);

    private ViewsHolder getViewHolder(int position,
                                      View convertView,
                                      ViewGroup parent) {
        return ViewsHolder.get(context, convertView, parent, itemLayoutId, position);
    }

}
