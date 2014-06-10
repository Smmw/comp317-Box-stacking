import java.util.LinkedList;
/**
 * A stack of boxes
 * This is a singly linked list of boxes that keeps track of the height
 * You can use a stack to create a new stack linked to the old one.
 * 	This way stacks are stored like a tree instead of separate lists.
 * It is the stacks job to ensure the stack is valid. You can't trust a box with
 * 	a job this important anyway.
 */
public class BoxStack implements Comparable{
	protected int height;
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
		if (top == null){
			return true;
		}
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
	public int getHeight(){
		return this.height;
	}

	public Box getTopBox(){
		if (top == null){
			return null;
		}
		return top.getBox();
	}

	/*
	 * Returns a linked list containing the boxes in decending order
	 */
	public LinkedList<Box> toList(){
		LinkedList<Box> list = new LinkedList<Box>();
		for (Node node = this.top; node != null; node = node.getBase()){
			list.offerFirst(node.getBox());
		}
		return list;
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

	/*
	 * Compares stacks by height
	 */
	@Override
	public int compareTo(Object o){
		BoxStack s = (BoxStack)o;
		int value = Integer.compare(this.height, s.getHeight());
		if (value != 0){
			return value;
		} else {
			if (this.getTopBox() == null){
				return s.getTopBox() == null ? 0 : 1;
			}
			return this.getTopBox().compareTo(s.getTopBox());
		}
	}
}
