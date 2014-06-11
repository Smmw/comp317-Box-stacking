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

    /**
     * Rotate the box by swapping the short side and the height
     */
    public void swapShort() {
	int tmp = this.shortSide;
	this.shortSide = this.height;
	this.height = tmp;

	fixBase();
    }

    /**
     * Rotate the box by swapping the long side and the height
     */
    public void swapLong() {
	int tmp = this.longSide;
	this.longSide = this.height;
	this.height = tmp;
	
	fixBase();
    }

    /**
     * Ensure the longSide is the long side, and shortSide is the
     * short side of the base.
     * This is useful after swapping one for the height to ensure
     * the values are still correct.
     */
    private void fixBase() {
	int tmp;

	if (this.shortSide > this.longSide) {
	    tmp = this.longSide;
	    this.longSide = this.shortSide;
	    this.shortSide = tmp;
	}
    }

	/*
	 * Compares the shortSide, longSide and height of the box
	 */
	@Override
	public int compareTo(Object o){
		Box b = (Box)o;
		int value = Integer.compare(this.shortSide, b.getShortSide());
		if (value != 0){
			return value;
		} else {
			value = Integer.compare(this.longSide, b.getLongSide());
			if (value != 0){
				return value;
			} else {
				return Integer.compare(this.height, b.getHeight());
			}
		}
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
