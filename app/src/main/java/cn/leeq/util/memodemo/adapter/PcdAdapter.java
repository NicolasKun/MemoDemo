package cn.leeq.util.memodemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.bean.AddrBean;

public class PcdAdapter extends BaseAdapter {
	private Context        mContext;
	private List<AddrBean> mInfos;

	public PcdAdapter(Context context, List<AddrBean> infos) {
		this.mContext = context;
		this.mInfos = infos;
	}

	@Override
	public int getCount() {
		
		return mInfos.size();
	}

	@Override
	public AddrBean getItem(int position) {
		return mInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pcd, parent,false);

			viewHolder = new ViewHolder(convertView);
			
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		AddrBean info = getItem(position);

		// 根据是否被选择来切换item的背景
		if(info.isChoose) {
			viewHolder.rl_name.setBackgroundResource(R.mipmap.choose_item_selected);
		}else {
			viewHolder.rl_name.setBackgroundColor(Color.WHITE);
		}
		
		viewHolder.tv_name.setText(info.getAddrName());
		
		return convertView;
	}
	
	private class ViewHolder {
		private TextView       tv_name;
		private RelativeLayout rl_name;

		public ViewHolder(View view) {
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			rl_name = (RelativeLayout) view.findViewById(R.id.rl_name);
		}
	}

}