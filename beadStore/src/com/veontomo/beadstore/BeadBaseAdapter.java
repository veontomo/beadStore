package com.veontomo.beadstore;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Adapter to visualize data stored in BeadInfo instances
 * 
 * @author veontomo@gmail.com
 * @since 0.6
 */
public class BeadBaseAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BeadInfo> mData;

	public BeadBaseAdapter(Context context, ArrayList<BeadInfo> data) {
		mContext = context;
		this.mData = data;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int pos) {
		return mData.get(pos);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override  
    public View getView(int position, View convertView, ViewGroup parent) {
		// !!! stub
         return null;  
    }  
}
