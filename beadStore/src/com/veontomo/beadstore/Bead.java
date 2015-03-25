package com.veontomo.beadstore;


/*
 * Class to describe beads (color, position)
 * @author veontomo@gmail.com
 * @since 0.1
 */
public class Bead {
	/**
	 * number of the color of the bead
	 * @since 0.1 
	 */
	private String colorCode;
	
	/**
	 * position of the bead on the stand
	 * @since 0.1
	 */
	private String location;
	
	/**
	 * Bead stand. It is responsible for finding the beads.
	 * @since 0.2
	 * @see BeadStand
	 */
	private BeadStand beadStand = new BeadStand();

	public Bead(String colorCode) {
		this.colorCode = colorCode;
		this.location = findPositionByColorNumber(colorCode);
	}
	
	
	/**
	 * Returns a position of a bead by its color code
	 * @since 0.1
	 */
	public String findPositionByColorNumber(String colorNumber){
		Location loc = beadStand.getByColor(colorNumber);
		if (loc != null){
			return loc.toString();
		}
		return "not found";
	}


	public String getColorCode() {
		return colorCode;
	}


	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}

}
