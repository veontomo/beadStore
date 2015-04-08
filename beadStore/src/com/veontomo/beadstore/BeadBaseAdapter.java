package com.veontomo.beadstore;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter to visualize data stored in BeadInfo instances
 * 
 * @author veontomo@gmail.com
 * @since 0.6
 */
public class BeadBaseAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BeadInfo> beadInfoBunch;
	
	/**
	 * Number of kinds of layout for bead visualization
	 * @since 0.6
	 */
	private final static int NUM_OF_TYPES = 1;
	
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
	private static final String TAG = "BeadStore";

	
	public BeadBaseAdapter(Context context, ArrayList<BeadInfo> beads) {
		this.mContext = context;
		this.beadInfoBunch = beads;
	}

	@Override
	public int getCount() {
		return beadInfoBunch.size();
	}

	@Override
	public Object getItem(int pos) {
		return beadInfoBunch.get(pos);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount(){
		return NUM_OF_TYPES;
	}
	
//	@Override
	public int getItemViewType(int index){
		BeadInfo beadInfo = this.beadInfoBunch.get(index);
		if (beadInfo.getType() == BeadInfo.AVAILABLE){
			
		}
		return 1;
	}
	
	@Override  
    public View getView(int position, View convertView, ViewGroup parent) {
		Log.i(TAG, "get view: position = " + String.valueOf(position));
		View row = convertView;
		if (row == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			row = inflater.inflate(LAYOUT_TEXT, parent, false);
		}
		TextView colorCodeTV = (TextView) row.findViewById(R.id.colorNumber);
		if (colorCodeTV != null){
			colorCodeTV.setText("+++");
		}
         return row;  
    }  
}
