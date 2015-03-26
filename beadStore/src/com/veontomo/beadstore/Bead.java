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
	

	public Bead(String colorCode) {
		this.colorCode = colorCode;
	}
	
	

	public String getColorCode() {
		return colorCode;
	}


	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

}
