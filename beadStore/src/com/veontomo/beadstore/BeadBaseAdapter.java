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
	 * Number of kinds of layout for bead visualization.
	 * <p>See <a href="http://stackoverflow.com/questions/5300962/getviewtypecount-and-getitemviewtype-methods-of-arrayadapter">this discussion</a>
	 * <p> Strange fact: if this constant is set to 1, it works well, but by the nature of this constant, it should be set to 4. But 
	 * in this case the program crashes. 
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
	private static final String TAG = "BeadStore";
	
	/**
	 * Constant representing status of a bead that is present at the store and available
	 * @since 0.6 
	 */
	private static final int AVAILABLE = 0;

	/**
	 * Constant representing status of a bead  information of which can not be retrieved.
	 * @since 0.6 
	 */

	private static final int UNKNOWN = 1;

	
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
	
    @Override
	public int getItemViewType(int index){
		BeadInfo beadInfo = this.beadInfoBunch.get(index);
		if (beadInfo.getStatus() == BeadInfo.AVAILABLE){
			return AVAILABLE;
		} 
		return UNKNOWN;
	}
	
	@Override  
    public View getView(int position, View convertView, ViewGroup parent) {
		Log.i(TAG, "get view: position = " + String.valueOf(position));
		View row = convertView;
		if (row == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (this.getItemViewType(position) == AVAILABLE){
				row = inflater.inflate(LAYOUT_TEXT, parent, false);
			} else {
				row = inflater.inflate(LAYOUT_THUMBNAIL, parent, false);
			}
		
		}
		TextView colorCodeTV = (TextView) row.findViewById(R.id.colorNumber);
		if (colorCodeTV != null){
			BeadInfo beadInfo = this.beadInfoBunch.get(position);
			String colorCode = beadInfo.getColorCode();
			if (colorCode != null){
				colorCodeTV.setText(colorCode);
			}
			
		}
         return row;  
    }  
}
