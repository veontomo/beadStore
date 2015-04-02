package com.veontomo.beadstore;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Adapter to fill in items of list view with bead data
 * 
 * @author veontomo@gmail.com
 * @since 0.1
 */
public class BeadAdapter extends ArrayAdapter<String> {
	private final static int BEAD_EXISTS = R.layout.bead_existing;
	private final static int BEAD_NOT_EXISTS = R.layout.bead_missing;

	private final static BeadStand beadStand = new BeadStand();

	/**
	 * An auxiliary string used to mark log messages during development stage.
	 * 
	 * @since 0.1
	 */
	final private String TAG = "BeadStore";

	Context context;
	ArrayList<String> data = null;
	
	/**
	 * A class that inserts bitmap into specified image view.
	 * @since 0.4
	 */
	BitmapInserter imageInserter = null;

	/**
	 * Constructor
	 * 
	 * @param Context
	 *            context
	 * @param int layoutId id of the layout in which info about each bead should
	 *        be loaded
	 * @param Bead
	 *            [] beads array of Bead instances
	 * @since 0.1
	 */
	public BeadAdapter(Context context, ArrayList<String> beadCodes) {
		super(context, BEAD_EXISTS, beadCodes);
		this.context = context;
		this.data = beadCodes;
	}

	@Override
	public View getView(int index, View rowView, ViewGroup parent) {
		View row = rowView;
		String beadColorCode = this.data.get(index);
		Location loc = null;
		loc = beadStand.getByColor(beadColorCode);
		LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();
		TextView beadColor;
		if (loc == null) {
			row = inflater.inflate(BEAD_NOT_EXISTS, parent, false);
			beadColor = (TextView) row.findViewById(R.id.colorNumber);
			beadColor.setText(beadColorCode);
			imageInserter = new BitmapInserter();
			imageInserter.setLocation((ImageView) row.findViewById(R.id.beadIconColumn));
			imageInserter.setStorage(new File(TAG, "thumbnails"));
			imageInserter.execute(beadColorCode);
			
		} else {
			row = inflater.inflate(BEAD_EXISTS, parent, false);
			beadColor = (TextView) row.findViewById(R.id.colorNumber);
			beadColor.setText(beadColorCode);

			TextView beadWing = (TextView) row
					.findViewById(R.id.beadLocationWing);
			beadWing.setText(loc.getWing());
			TextView beadColumn = (TextView) row
					.findViewById(R.id.beadLocationColumn);
			beadColumn.setText(String.valueOf(loc.getCol()));

			TextView beadRow = (TextView) row
					.findViewById(R.id.beadLocationRow);
			beadRow.setText(String.valueOf(loc.getRow()));

		}

		return row;
	}

}
