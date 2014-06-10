/**
 * A box
 */
public class Box{
	private double height;
	private double longSide;
	private double shortSide;

	/*
	 * Makes a box, will make the long side the long side on it's own.
	 * It doesn't trust you anyway.
	 */
	public Box(double height, double side1, double side2){
		this.height = height;
		this.longSide = side1 > side2 ? side1 : side2;
		this.shortSide = side2 > side1 ? side1 : side2;
	}

	/*
	 * Returns the height of the box
	 */
	public double getHeight(){
		return this.height;
	}

	/*
	 * Returns the long side
	 */
	public double getLongSide(){
		return this.longSide;
	}

	/* 
	 * Returns the short side
	 */
	public double getShortSide(){
		return this.shortSide;
	}
}
