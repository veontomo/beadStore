package com.veontomo.beadstore;

import android.content.Context;
import android.util.Log;

/**
 * Collects information about specific bead.
 * It contains information that should be displayed
 * on single row of bead list view. 
 * @author veontomo@gmail.com
 * @since 0.3
 */
public class BeadInfo {
	
	/**
	 * Constant representing status of a bead that is present at the store and available
	 * @since 0.6 
	 */
	public static final int AVAILABLE = 1;
	
	/**
	 * Constant representing status of a bead that is not present at the store but it 
	 * becomes available soon. 
	 * @since 0.6 
	 */
	public static final int EXHAUSTED = 2;
	
	/**
	 * Constant representing status of a bead that is not present at the store, not available
	 * in the forthcoming future, but it is present in some external store.
	 * @since 0.6 
	 */
	public static final int EXTERNAL = 3;

	
	/**
	 * Constant representing status of a bead  information of which can not be retrieved.
	 * @since 0.6 
	 */
	public static final int UNKNOWN = 4;


	/**
	 * Code of color of a bead
	 * @since 0.3
	 */
	private String colorCode;
	
	/**
	 * Location of the bead on the bead stand.
	 * @see BeadStore
	 * @since 0.3
	 */
	private Location location;
	
	/**
	 * Bead status: available, sold out, external, unknown.
	 * @since 0.6 
	 */
	private int status;
	
	/**
	 * Bead instance.
	 * 
	 * It serves to have access to bead-specific information (color, type, etc.)
	 * @since 0.7 
	 */
	private Bead bead;

	/**
	 * Number of bead sachets present in the store
	 * @since 0.7
	 */
	private int quantity;

	/**
	 * Context of the current activity. It is set from BeadActivity
	 * @since 0.7
	 */
	private Context context;  
	
	/**
	 * Context getter
	 * @return the context
	 * @since 0.7
	 */
	public Context getContext() {
		return context;
	}


	/**
	 * BeadStand instance.
	 * 
	 * It serves to have access to bead-store-related information 
	 * (quantity, location, etc.)
	 * @since 0.7 
	 */
	private static BeadStore beadStore = null;

	private static final String TAG = "BeadStore"; 
	
	/**
	 * Constructor
	 * @param colorCode
	 * @since 0.6
	 */
	public BeadInfo(String colorCode, Context context){
		this.context = context;
		this.beadStore = new BeadStore(context);
		this.bead = new Bead(colorCode);
		this.setColorCode(this.bead.getColorCode());
		this.setLocation(beadStore.getByColor(this.colorCode));
		this.setQuantity(beadStore.getQuantity(this.colorCode));
		this.updateStatus();
	}


	/**
	 * Calculate status of the bead based on the location (whether it is present in the store) 
	 * and quantity (how many sachets are present in the store)
	 * @since 0.7
	 */
	private void updateStatus() {
		if (this.getLocation() == null){
			this.setStatus(EXTERNAL);
		} else {
			if (this.getQuantity() == 0){
				this.setStatus(EXHAUSTED);
			} else {
				this.setStatus(AVAILABLE);
			}
		}
		
	}


	/**
	 * status setter
	 * @param int 
	 * @since 0.7
	 * 
	 */
	private void setStatus(int status) {
		this.status = status;
		
	}


	/**
	 * quantity setter
	 *  
	 * @param quantity
	 * @since 0.7
	 */
	private void setQuantity(int quantity) {
		this.quantity = quantity;
		
	}
	
	/**
	 * quantity getter 
	 * @return int
	 * @since 0.7
	 */
	public int getQuantity(){
		return this.quantity;
	}
	
	




	/**
	 * color code getter.
	 * @return colorCode
	 * @since 0.3
	 */
	public String getColorCode() {
		return colorCode;
	}

	/**
	 * location getter
	 * @return location
	 * @since 0.3
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * color code setter
	 * @param colorCode
	 * @since 0.3
	 */
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	/**
	 * location setter
	 * @param location
	 * @since 0.3
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	
	public String toString(){
		String output = "";
		if (colorCode != null){
			output += colorCode + " ";
		}
		if (location != null){
			output += location.toString() + " ";
		}
		return output;
	}

	/**
	 * Returns type of the bead
	 * 
	 * @return int
	 * @since 0.6
	 */
	public int getStatus() {
		return (this.getLocation() == null) ? UNKNOWN : AVAILABLE;
	}
}
