package com.veontomo.beadstore;

public class Bead {
	/**
	 * number of the color of the bead 
	 */
	private String colorCode;
	
	/**
	 * position of the bead on the stand
	 */
	private String location;

	public Bead(String colorCode) {
		this.colorCode = colorCode;
		this.location = findPositionByColorNumber(colorCode);
	}
	
	public static String findPositionByColorNumber(String colorNumber){
		return "a stub";
	}

}
