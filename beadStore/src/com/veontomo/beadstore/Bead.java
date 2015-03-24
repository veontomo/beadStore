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

	public Bead(String colorCode) {
		this.colorCode = colorCode;
		this.location = findPositionByColorNumber(colorCode);
	}
	
	
	/**
	 * Returns a position of a bead by its color code
	 * @since 0.1
	 */
	public static String findPositionByColorNumber(String colorNumber){
		return "a stub";
	}

}
