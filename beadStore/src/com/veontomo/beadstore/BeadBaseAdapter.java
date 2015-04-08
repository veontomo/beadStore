package com.veontomo.beadstore;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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
	 Context mContext;
	private ArrayList<Bead> beads;
	
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

	
	public BeadBaseAdapter(Context context, ArrayList<Bead> beads) {
		mContext = context;
		this.beads = beads;
	}

	@Override
	public int getCount() {
		return beads.size();
	}

	@Override
	public Object getItem(int pos) {
		return beads.get(pos);
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
		View row = convertView;
		if (row == null){
			LayoutInflater inflater = ((Activity) this.mContext).getLayoutInflater();
			row = inflater.inflate(LAYOUT_TEXT, parent, false);
		}
		TextView colorCodeTV = (TextView) row.findViewById(R.id.colorNumber);
		if (colorCodeTV != null){
			colorCodeTV.setText("+++");
		}
         return row;  
    }  
}
