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
	
	

}
