package cn.leeq.util.memodemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.bean.DynamicBean;

/**
 * Created by LeeQ on 2016-10-22.
 * Use :
 */

public class DynamicAdapter extends BaseAdapter {
    private List<DynamicBean> data;
    private Context context;
    private OnClickDynamicItemListener listener;

    public DynamicAdapter(List<DynamicBean> data, Context context, OnClickDynamicItemListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_dynamic_insert_list, null);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        }else
            holder = (ViewHolder) convertView.getTag();

        String content = data.get(position).getContent();
        holder.itemEtContent.setText(content);

        holder.itemBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onScan(position);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        EditText itemEtContent;
        ImageView itemBtnScan;

        public void initView(View view) {
            itemBtnScan = (ImageView) view.findViewById(R.id.item_di_list_btn_scan);
            itemEtContent = (EditText) view.findViewById(R.id.item_di_list_et_content);
        }
    }

    public interface OnClickDynamicItemListener {
        void onScan(int position);
    }
}

