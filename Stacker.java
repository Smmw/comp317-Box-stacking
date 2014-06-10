import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
/**
 * Stacks boxes of fixed orientation using the Principle of Optimality (All things going well)
 * This goes as follows
 * The Stacker is passed a collection of boxes, these boxes have a right way up
 * The collection is ordered by the longest side on the base of the box as a tree set
 * An empty stack is made
 * This stack is placed in another Tree set
 * The boxes are traversed from largest to smallest
 * 	A new stack is formed by placing the box on top of the tallest stack it fits on
 * 	The new stack is added to the stacks
 * The traversal ends
 * The tallest stack if taken from the stacks
 * This stack is the optimal stack (Again all things going well)
 * 
 * The idea is the optimal stack for the box is a subset of the optimal stack over all if
 * 	the box is in the optimal stack
 *
 * Remember the stacker does not rotate boxes.
 */
public class Stacker{
	public BoxStack stack(Collection<Box> boxes){
		// Get the tree of boxes so they can be navagated in order
		TreeSet<Box> boxTree = new TreeSet<Box>(boxes);
		TreeSet<BoxStack> stackTree = new TreeSet<BoxStack>();
		// Make the empty stack. This is the root of all stacks. Very important.
		stackTree.add(new BoxStack());
		// Stack some boxes
		// Each box in decending order
		for (Box box : boxTree){
			// Stack on largest valid stack
			for (BoxStack stack : stackTree.descendingSet()){
				if (stack.fits(box)){
					stackTree.add(stack.stack(box));
					break;
				}
			}
		}
		// Return the best stack
		return stackTree.last();
	}
}
