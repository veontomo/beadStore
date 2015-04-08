package com.veontomo.beadstore;

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
	 * @see BeadStand
	 * @since 0.3
	 */
	private Location location;
	
	/**
	 * Bead status: available, sold out, external, unknown.
	 * @since 0.6 
	 */
	private int status;
	
	/**
	 * Constructor
	 * @param colorCode
	 * @since 0.6
	 */
	public BeadInfo(String colorCode){
		this.setColorCode(colorCode);
		
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
		return (!this.colorCode.equals("1")) ? UNKNOWN : AVAILABLE;
	}
	
	

}
