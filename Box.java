/**
 * A box
 */
public class Box implements Comparable{
	private int height;
	private int longSide;
	private int shortSide;

	/*
	 * Makes a box, will make the long side the long side on it's own.
	 * It doesn't trust you anyway.
	 */
	public Box(int height, int side1, int side2){
		this.height = height;
		this.longSide = side1 > side2 ? side1 : side2;
		this.shortSide = side2 > side1 ? side1 : side2;
	}

	/*
	 * Returns the height of the box
	 */
	public int getHeight(){
		return this.height;
	}

	/*
	 * Returns the long side
	 */
	public int getLongSide(){
		return this.longSide;
	}

	/* 
	 * Returns the short side
	 */
	public int getShortSide(){
		return this.shortSide;
	}

	/*
	 * Compares the longSide of the box
	 */
	@Override
	public int compareTo(Object o){
		Box b = (Box)o;
		return Integer.compare(this.longSide, b.getLongSide());
	}

    /**
     * Returns a string representation of the box
     */
    @Override
    public String toString() {
	return String.format("Box <%d,%d,%d>",
			     this.height,
			     this.longSide,
			     this.shortSide);
    }
}
