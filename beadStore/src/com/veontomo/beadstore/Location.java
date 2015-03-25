package com.veontomo.beadstore;

/**
 * Coordinates of the bead on the stand
 * 
 * @author veontomo@gmail.com
 * @since 0.2
 */
public class Location {
	/**
	 * Name of the stand wing on which the bead is located
	 * 
	 * @since 0.2
	 */
	private String wing;

	/**
	 * Wing getter.
	 * 
	 * @return wing
	 * @since 0.3
	 */
	public String getWing() {
		return wing;
	}

	/**
	 * Row getter
	 * @return row
	 * @since 0.3
	 */
	public int getRow() {
		return row;
	}

	/**
	 * col getter
	 * @return col
	 * @since 0.3
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Wing setter
	 * @since 0.3
	 * @param wing 
	 */
	public void setWing(String wing) {
		this.wing = wing;
	}

	/**
	 * Row setter
	 * @param row
	 * @since 0.3
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * Col setter
	 * @param col
	 * @since 0.3
	 */
	public void setCol(int col) {
		this.col = col;
	}

	/**
	 * row number in which the bead is located
	 * 
	 * @since 0.2
	 */
	private int row;

	/**
	 * column number in which the bead is located
	 * 
	 * @since 0.2
	 */
	private int col;

	public Location(String wing, int row, int col) {
		this.wing = wing;
		this.row = row;
		this.col = col;
	}
	
	public String toString(){
		return wing + ": " + String.valueOf(row) + " - " + String.valueOf(col);
	}

}