package com.veontomo.beadstore;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter to fill in items of list view with bead data 
 * @author veontomo@gmail.com
 * @since 0.1
 */
public class BeadAdapter extends ArrayAdapter<Bead> {
	private static class BeadHolder {
		TextView colorCode;
		TextView location;
	}
	Context context;
    int layoutId;   
    Bead data[] = null;
	/**
	 *  Constructor
	 *  @param Context context 
	 *  @param int layoutId     id of the layout in which info about each bead should be loaded  
	 *  @param Bead[] beads     array of Bead instances
	 *  @since 0.1
	 */
	public BeadAdapter(Context context, int layoutId, Bead[] beads) {
		 super(context, layoutId, beads);
		 this.context = context;
		 this.layoutId = layoutId;
		 this.data = beads;
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
            holder.location = (TextView) row.findViewById(R.id.beadPosition);
            row.setTag(holder);
		} else {
			holder = (BeadHolder) row.getTag();
		}
		Bead bead = this.data[index];
		holder.colorCode.setText(bead.getColorCode());
        holder.location.setText(bead.getLocation());
		
		return row;
	}

}
