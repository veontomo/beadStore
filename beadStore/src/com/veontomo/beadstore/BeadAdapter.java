package com.veontomo.beadstore;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Adapter to fill in items of list view with bead data 
 * @author veontomo@gmail.com
 * @since 0.1
 */
public class BeadAdapter extends ArrayAdapter<BeadInfo> {
	private static class BeadHolder {
		TextView colorCode;
		TextView wing;
		TextView row;
		TextView col;
	}
	Context context;
    int layoutId;   
    ArrayList<BeadInfo> data = null;
	/**
	 *  Constructor
	 *  @param Context context 
	 *  @param int layoutId     id of the layout in which info about each bead should be loaded  
	 *  @param Bead[] beads     array of Bead instances
	 *  @since 0.1
	 */
	public BeadAdapter(Context context, int layoutId, ArrayList<BeadInfo> beadInfo) {
		 super(context, layoutId, beadInfo);
		 this.context = context;
		 this.layoutId = layoutId;
		 this.data = beadInfo;
	}
	
	@Override
	public View getView(int index, View rowView, ViewGroup parent){
		View row = rowView;
		BeadHolder holder = null;
		
		if (row == null){
			LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();
            row = inflater.inflate(layoutId, parent, false);
           
            holder = new BeadHolder();
            holder.colorCode = (TextView) row.findViewById(R.id.colorNumber);
            holder.wing = (TextView) row.findViewById(R.id.beadLocationWing);
            holder.row =  (TextView) row.findViewById(R.id.beadLocationRow);
            holder.col =  (TextView) row.findViewById(R.id.beadLocationColumn);
            row.setTag(holder);
		} else {
			holder = (BeadHolder) row.getTag();
		}
		BeadInfo beadInfo = this.data.get(index);
		holder.colorCode.setText(beadInfo.getColorCode());
		Location loc = beadInfo.getLocation();
		if (loc != null){
			fillInLocationInfo(holder, loc);
		} else {
			fillInLocationNotFound(holder, context.getResources().getString(R.string.beadNotFound));
		}
		
		return row;
	}
	
	private void  fillInLocationInfo(BeadHolder holder, Location loc){
        holder.wing.setText(loc.getWing());
        holder.row.setText(String.valueOf(loc.getRow()));
        holder.col.setText(String.valueOf(loc.getCol()));
	}
	
	private void  fillInLocationNotFound(BeadHolder holder, String message){
        holder.wing.setText(message);
        holder.row.setText("");
        holder.col.setText("");
	}

	

}
