/**
 * A stack of boxes
 * This is a singly linked list of boxes that keeps track of the height
 * You can use a stack to create a new stack linked to the old one.
 * 	This way stacks are stored like a tree instead of separate lists.
 * It is the stacks job to ensure the stack is valid. You can't trust a box with
 * 	a job this important anyway.
 */
public class BoxStack{
	protected double height;
	protected Node top; 	// I also want to call it head, leaf and tail
							// it depends how you think about it
	/*
	 * Makes an empty box stack
	 */
	public BoxStack(){
		this.height = 0;
		this.top = null;
	}
	/*
	 * Makes a box stack from an existing base
	 */
	protected BoxStack(Box box, BoxStack base){
		this.height = base.getHeight() + box.getHeight();
		this.top = new Node(box, base.getNodes());
	}

	/*
	 * Tells you if the box may sit atop the stack
	 */
	public boolean fits(Box box){
		Box base = top.getBox();
		return box.getLongSide() < base.getLongSide()
				&& box.getShortSide() < base.getShortSide();
	}
	
	/*
	 * Adds a box to the stack
	 */
	public BoxStack stack(Box box){
		// Sanity check, unnessicary if the program is sane.
		// TODO: Decide if it is worth removing later
		if (!this.fits(box)){
			throw new RuntimeException("Attempted to stack large box on small base!");
		}
		return new BoxStack(box, this);
	}

	/*
	 * Returns the height of the stack
	 */
	public double getHeight(){
		return this.height;
	}
	
	/*
	 * Returns the top node of the stack
	 */
	protected Node getNodes(){
		return this.top;
	}

	/**
	 * A link in the list that is a stack. 
	 * Null terminated.
	 */
	private class Node{
		private Box box;
		private Node base;
		
		/*
		 * Make a node
		 */
		public Node(Box box, Node base){
			this.box = box;
			this.base = base;
		}

		/*
		 * Get the box
		 */
		public Box getBox(){
			return this.box;
		}

		/*
		 * Get the base
		 */
		public Node getBase(){
			return this.base;
		}
	}
}
