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
	
	/**
	 * Number of kinds of layout for bead visualization
	 * @since 0.6
	 */
	private final static int NUM_OF_TYPES = 2;
	
	/**
	 * Layout with textual information
	 * @since 0.6
	 */
	private final static int LAYOUT_TEXT = R.layout.bead_text;


	/**
	 * Layout with graphical information
	 * @since 0.6
	 */
	private final static int LAYOUT_THUMBNAIL = R.layout.bead_thumbnail;

	
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
	public int getViewTypeCount(){
		return NUM_OF_TYPES;
	}
	
	@Override  
    public View getView(int position, View convertView, ViewGroup parent) {
		// !!! stub
         return null;  
    }  
}
