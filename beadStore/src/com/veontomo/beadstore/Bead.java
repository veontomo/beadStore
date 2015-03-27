package com.veontomo.beadstore;

import java.io.File;


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
	 * Constructor
	 * @param colorCode
	 * @since 0.1
	 */
	public Bead(String colorCode) {
		this.colorCode = colorCode;
	}
	
	
	/**
	 * colorCode getter
	 * @return String
	 * @since 0.1
	 * @see Bead#colorCode
	 */
	public String getColorCode() {
		return colorCode;
	}

	/**
	 * colorCode setter
	 * @param colorCode
	 * @since 0.1
	 * @see Bead#colorCode
	 */
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	/**
	 * Returns file of graphic representation of the bead.
	 * 
	 * The images are located on the site of Preciosa. For example, images of bead with color code 90090 can be found here:
	 * <p>icon size: http://www.preciosaornela.com/catalog/jablonex_traditional_czech_beads/img/rocailles/prod/thread/90090.jpg
	 * <p>full size: http://www.preciosaornela.com/catalog/jablonex_traditional_czech_beads/img/rocailles/prod/detail/90090_det.jpg
	 * @return File
	 * @since 0.2.1
	 */
	public File getImage(){
		/// !!! stub
		return new File("");
	}
	
	public static String canonicalColorCode(String color){
		return color.replaceAll("(\\d+)[\\s|-]+(\\d+)", "$1/$2");
	}
}
